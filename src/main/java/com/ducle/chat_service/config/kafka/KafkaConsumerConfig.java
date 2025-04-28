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

import com.ducle.chat_service.model.dto.CommandDTO;
import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.model.dto.UserPresenceDTO;

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

    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> valueType) {
        JsonDeserializer<T> deserializer = new JsonDeserializer<>(valueType);
        deserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(
                configProperties(),
                new StringDeserializer(),
                deserializer);
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T> createKafkaListenerFactory(Class<T> valueType) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(createConsumerFactory(valueType));
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageDTO> messageKafkaListenerFactory() {
        return createKafkaListenerFactory(MessageDTO.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CommandDTO> commandKafkaListenerFactory() {
        return createKafkaListenerFactory(CommandDTO.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserPresenceDTO> presenceKafkaListenerFactory() {
        return createKafkaListenerFactory(UserPresenceDTO.class);
    }
}
