package com.ducle.chat_service.config.kafka;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.ducle.chat_service.model.dto.CommandDTO;
import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.model.dto.UserPresenceDTO;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-config")
public class KafkaProducerConfig {

        @Value(value = "${spring.kafka.producer.bootstrap-servers}")
        private String bootstrapAddress;

        private String chatTopicName;
        private int chatPartitions;
        private int chatReplicas;

        private String commandTopicName;
        private int commandPartitions;
        private int commandReplicas;

        private String presenceTopicName;
        private int presencePartitions;
        private int presenceReplicas;

        private Map<String, Object> producerConfigs() {
                Map<String, Object> configProps = new HashMap<>();
                configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
                configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
                configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
                return configProps;
        }

        private <T> ProducerFactory<String, T> createProducerFactory() {
                return new DefaultKafkaProducerFactory<>(producerConfigs());
        }

        private <T> KafkaTemplate<String, T> createKafkaTemplate() {
                return new KafkaTemplate<>(createProducerFactory());
        }

        private NewTopic createTopic(String topicName, int partitions, int replicas) {
                return TopicBuilder.name(topicName)
                                .partitions(partitions)
                                .replicas(replicas)
                                .config(TopicConfig.RETENTION_MS_CONFIG, "604800000") // 7 days
                                .config(TopicConfig.CLEANUP_POLICY_CONFIG, TopicConfig.CLEANUP_POLICY_DELETE)
                                .build();
        }

        @Bean
        public KafkaTemplate<String, MessageDTO> kafkaTemplate() {
                return createKafkaTemplate();
        }

        @Bean
        public KafkaTemplate<String, CommandDTO> commandKafkaTemplate() {
                return createKafkaTemplate();
        }

        @Bean
        public KafkaTemplate<String, UserPresenceDTO> presenceKafkaTemplate() {
                return createKafkaTemplate();
        }

        @Bean
        public NewTopic chatTopic() {
                return createTopic(chatTopicName, chatPartitions, chatReplicas);
        }

        @Bean
        public NewTopic commandTopic() {
                return createTopic(commandTopicName, commandPartitions, commandReplicas);
        }

        @Bean
        public NewTopic presenceTopic() {
                return createTopic(presenceTopicName, presencePartitions, presenceReplicas);
        }
}
