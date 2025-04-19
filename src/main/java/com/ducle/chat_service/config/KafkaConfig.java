package com.ducle.chat_service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.ducle.chat_service.model.dto.MessageDTO;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaConfig {

        @Value(value = "${spring.kafka.producer.bootstrap-servers}")
        private String bootstrapAddress;

        private String topicName;
        private int partitions;
        private int replicas;

        @Bean
        public ProducerFactory<String, MessageDTO> producerFactory() {
                Map<String, Object> configProps = new HashMap<>();
                configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
                configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
                return new DefaultKafkaProducerFactory<>(configProps);
        }

        @Bean
        public KafkaTemplate<String, MessageDTO> kafkaTemplate() {
                return new KafkaTemplate<>(producerFactory());
        }

        @Bean
        public KafkaAdmin kafkaAdmin() {
                Map<String, Object> configs = new HashMap<>();
                configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
                return new KafkaAdmin(configs);
        }

        @Bean
        public NewTopic chatTopic() {
                return TopicBuilder.name(topicName)
                                .partitions(partitions)
                                .replicas(replicas)
                                .config(TopicConfig.RETENTION_MS_CONFIG, "604800000")
                                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                                .build();
        }

}
