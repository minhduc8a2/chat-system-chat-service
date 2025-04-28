package com.ducle.chat_service.service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.config.kafka.KafkaProducerConfig;
import com.ducle.chat_service.model.dto.MessageDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaChatProducerService {
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;
    private final KafkaProducerConfig kafkaConfig;

    public void sendMessage(MessageDTO message) {
        kafkaTemplate.send(kafkaConfig.getChatTopicName(), message.roomId().toString(), message);
    }

}
