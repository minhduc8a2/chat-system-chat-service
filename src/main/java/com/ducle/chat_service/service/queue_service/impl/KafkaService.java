package com.ducle.chat_service.service.queue_service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.config.KafkaConfig;
import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.service.queue_service.QueueService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaService implements QueueService {
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;
    private final KafkaConfig kafkaConfig;

    public void sendMessage(MessageDTO message) {
        kafkaTemplate.send(kafkaConfig.getTopicName(), message.roomId().toString(), message);
    }

}
