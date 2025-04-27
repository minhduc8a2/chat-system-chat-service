package com.ducle.chat_service.config.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.ducle.chat_service.model.dto.MessageDTO;

@Configuration
public class KafkaConsumerConfig {
    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    public Map<String, Object> configProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean
    public ConsumerFactory<String, MessageDTO> messageConsumerFactory() {
        JsonDeserializer<MessageDTO> deserializer = new JsonDeserializer<>(MessageDTO.class);
        deserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(
                configProperties(),
                new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageDTO> messageKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageConsumerFactory());
        return factory;
    }

}
