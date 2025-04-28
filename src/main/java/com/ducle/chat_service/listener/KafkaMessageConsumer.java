package com.ducle.chat_service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.model.dto.MessageDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageConsumer {
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "${kafka-config.chat-topic-name}", containerFactory = "messageKafkaListenerFactory")
    public void consume(MessageDTO message) {
        log.info("Message consumed: {}", message);
        messagingTemplate.convertAndSend("/topic/chat_room/" + message.roomId(), message);
    }

}