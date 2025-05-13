package org.mcolomerc.kafka_consumer.listener;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaConsumerService {

    private final KafkaConfig kafkaConfig;

    private ConcurrentMessageListenerContainer<String, String> container;

    private static final String LISTENER_ID = "dynamicListener";

    public KafkaConsumerService(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    @PostConstruct
    public void init() {
        System.out.println (" ====> Starting Kafka listener with config: " + kafkaConfig);
        startListener();
    }

    public void restartListener() {
        if (container != null) {
            CountDownLatch latch = new CountDownLatch(1);
            container.stop(latch::countDown);

            try {
                latch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        startListener();
    }

    private void startListener() {
        ContainerProperties containerProps = new ContainerProperties(
                kafkaConfig.getTopics().toArray(new String[0])
        );
        containerProps.setGroupId(kafkaConfig.getGroupId());
        containerProps.setMessageListener((MessageListener<String, String>) this::handleMessage);

        this.container = new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProps);
        container.setAutoStartup(true);
        container.start();
    }

    private ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfig.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfig.getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // Optional SASL configuration
        if (kafkaConfig.getSaslJaasConfig() != null && !kafkaConfig.getSaslJaasConfig().isBlank()) {
            props.put("security.protocol", "SASL_SSL"); // or SASL_PLAINTEXT depending on your setup
            props.put("sasl.mechanism", "PLAIN"); // adjust based on your Kafka setup
            props.put("sasl.jaas.config", kafkaConfig.getSaslJaasConfig());
        }

        return new DefaultKafkaConsumerFactory<>(props);
    }

    private void handleMessage(ConsumerRecord<String, String> record) {
        System.out.printf("====> Received message: %s from topic: %s%n", record.value(), record.topic());
    }
}