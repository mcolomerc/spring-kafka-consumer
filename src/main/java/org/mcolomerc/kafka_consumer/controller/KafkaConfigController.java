package org.mcolomerc.kafka_consumer.controller;

import org.mcolomerc.kafka_consumer.listener.KafkaConfig;
import org.mcolomerc.kafka_consumer.listener.KafkaConsumerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka/config")
public class KafkaConfigController {

    private final KafkaConfig kafkaConfig;
    private final KafkaConsumerService kafkaConsumerService;

    public KafkaConfigController(KafkaConfig kafkaConfig, KafkaConsumerService kafkaConsumerService) {
        this.kafkaConfig = kafkaConfig;
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @PostMapping("/update")
    public String updateConfig(
            @RequestBody KafkaConfig newConfig
    ) {
        System.out.println ("====> Update Kafka listener with new config: " + newConfig);
        kafkaConfig.setGroupId(newConfig.getGroupId());
        kafkaConfig.setBootstrapServers(newConfig.getBootstrapServers());
        kafkaConfig.setSaslJaasConfig(newConfig.getSaslJaasConfig());
        kafkaConfig.setTopics(newConfig.getTopics());
        System.out.println ("====> Restart Kafka listener with config: " + kafkaConfig);
        kafkaConsumerService.restartListener();
        return "Kafka listener restarted with new config.";
    }
    @GetMapping
    public KafkaConfig getConfig() {
        return kafkaConfig;
    }
}