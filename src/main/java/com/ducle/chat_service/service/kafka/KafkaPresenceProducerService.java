package com.ducle.chat_service.service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.config.kafka.KafkaProducerConfig;
import com.ducle.chat_service.model.dto.CommandDTO;
import com.ducle.chat_service.model.dto.UserPresenceDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaPresenceProducerService {
    private final KafkaTemplate<String, UserPresenceDTO> kafkaTemplate;
    private final KafkaProducerConfig kafkaConfig;

    public void sendMessage(UserPresenceDTO userPresenceDTO) {
        kafkaTemplate.send(kafkaConfig.getPresenceTopicName(), userPresenceDTO.id().toString(), userPresenceDTO);
    }

}
