package com.ducle.chat_service.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.ducle.chat_service.model.dto.ClientMessageDTO;
import com.ducle.chat_service.model.dto.CommandDTO;
import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.model.dto.UserPresenceDTO;
import com.ducle.chat_service.model.enums.CommandType;
import com.ducle.chat_service.model.enums.MessageType;
import com.ducle.chat_service.security.ChatRoomSecurity;
import com.ducle.chat_service.service.id_generator.impl.Snowflake;
import com.ducle.chat_service.service.kafka.KafkaChatProducerService;
import com.ducle.chat_service.service.kafka.KafkaCommandProducerService;
import com.ducle.chat_service.service.kafka.KafkaPresenceProducerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final KafkaChatProducerService chatQueueService;
    private final KafkaCommandProducerService commandQueueService;
    private final KafkaPresenceProducerService presenceQueueService;
    private final Snowflake snowflake;

    private final ChatRoomSecurity chatRoomSecurity;

    public void sendMessageToChatroom(Long chatroomId, Long userId, ClientMessageDTO message) {

        chatRoomSecurity.ensureAccessToRoom(userId, chatroomId);

        MessageDTO trustedMessage = new MessageDTO(snowflake.generateId(), userId, chatroomId, message.content(),
                MessageType.GROUP,
                Instant.now());
        chatQueueService.sendMessage(trustedMessage);
    }

    public MessageDTO createSystemMessage(String content, long chatRoomId, MessageType type) {
        return new MessageDTO(snowflake.generateId(), 0L, chatRoomId, content, type, Instant.now());
    }

    public void sendSystemMessageToChatroom(String content, long chatRoomId, MessageType type) {
        chatQueueService.sendMessage(createSystemMessage(content, chatRoomId, type));
    }

    public void sendCommandMessageToChatroom(String payload, long chatRoomId, CommandType type) {
        commandQueueService.sendMessage(new CommandDTO(chatRoomId, type, payload));
    }

    public void sendUserPresenceToUserTopic(Long userId, boolean isOnline) {
        presenceQueueService.sendMessage(new UserPresenceDTO(userId, isOnline));
    }

}
