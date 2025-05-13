# spring-kafka-consumer
Spring Kafka consumer with update config endpoint 
 
Provides an endpoint to update the Kafka config and restarts the Spring container. 

Implemented only with: 

```
 security.protocol = SASL_SSL 
 sasl.mechanism = PLAIN 
```

Update Configuration: 

```sh  
curl -X POST http://localhost:8080/kafka/config/update \
-H "Content-Type: application/json" \
-d '{
"groupId": "spring-kafka-consumer",
"topics": ["topic_11"],
"bootstrapServers": "pkc-zpjg0.eu-central-1.aws.confluent.cloud:9092",
"saslJaasConfig": "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"API_KEY\" password=\"API_SECRET\";"
}'
```
