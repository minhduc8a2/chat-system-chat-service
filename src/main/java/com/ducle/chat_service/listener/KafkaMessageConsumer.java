package com.ducle.chat_service.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.model.dto.CommandDTO;
import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.model.dto.UserPresenceDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageConsumer {
    private static final String ONLINE_STATUS_DESTIONATION = "/topic/presence";

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "${kafka-config.chat-topic-name}", containerFactory = "messageKafkaListenerFactory")
    public void consumeChat(MessageDTO message) {
        log.info("Message consumed: {}", message);
        messagingTemplate.convertAndSend("/topic/chat_room/" + message.roomId(), message);
    }

    @KafkaListener(topics = "${kafka-config.command-topic-name}", containerFactory = "commandKafkaListenerFactory")
    public void consumeCommand(CommandDTO command) {
        log.info("Command consumed: {}", command);
        messagingTemplate.convertAndSend("/topic/chat_room/" + command.chatRoomId() + "/commands", command);
    }

    @KafkaListener(topics = "${kafka-config.presence-topic-name}", containerFactory = "presenceKafkaListenerFactory")
    public void consumepresence(UserPresenceDTO userPresenceDTO) {
        log.info("userPresenceDTO consumed: {}", userPresenceDTO);
        messagingTemplate.convertAndSend(ONLINE_STATUS_DESTIONATION + "/" + userPresenceDTO.id(), userPresenceDTO);
    }

}