package com.ducle.chat_service.service.queue_service.impl;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.config.kafka.KafkaProducerConfig;
import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.service.queue_service.QueueProducerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaProducerService implements QueueProducerService {
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;
    private final KafkaProducerConfig kafkaConfig;

    public void sendMessage(MessageDTO message) {
        kafkaTemplate.send(kafkaConfig.getTopicName(), message.roomId().toString(), message);
    }

}
