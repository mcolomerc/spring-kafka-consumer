package org.mcolomerc.kafka_consumer.listener;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class KafkaConfig {
    @Value( "${spring.kafka.consumer.properties.group.id}" )
    private String groupId;
    @Value( "${spring.kafka.consumer.properties.topics}" )
    private List<String> topics =  new ArrayList<>();
    @Value( "${spring.kafka.consumer.bootstrap-servers}" )
    private String bootstrapServers;
    @Value( "${spring.kafka.consumer.properties.sasl.jaas.config}" )
    private String saslJaasConfig;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getSaslJaasConfig() {
        return saslJaasConfig;
    }

    public void setSaslJaasConfig(String saslJaasConfig) {
        this.saslJaasConfig = saslJaasConfig;
    }

    @Override
    public String toString() {
        return "KafkaConfig{" +
                "groupId='" + groupId + '\'' +
                ", topics=" + topics +
                ", bootstrapServers='" + bootstrapServers + '\'' +
                ", saslJaasConfig='" + saslJaasConfig + '\'' +
                '}';
    }

}
