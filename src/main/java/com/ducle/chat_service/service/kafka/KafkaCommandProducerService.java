package com.ducle.chat_service.service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.config.kafka.KafkaProducerConfig;
import com.ducle.chat_service.model.dto.CommandDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaCommandProducerService {
    private final KafkaTemplate<String, CommandDTO> kafkaTemplate;
    private final KafkaProducerConfig kafkaConfig;

    public void sendMessage(CommandDTO message) {
        kafkaTemplate.send(kafkaConfig.getCommandTopicName(), message.chatRoomId().toString(), message);
    }

}
