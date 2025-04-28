package com.ducle.chat_service.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.ducle.chat_service.model.dto.ClientMessageDTO;
import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.model.enums.MessageType;
import com.ducle.chat_service.security.ChatRoomSecurity;
import com.ducle.chat_service.service.id_generator.impl.Snowflake;
import com.ducle.chat_service.service.queue_service.QueueProducerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final QueueProducerService queueService;
    private final Snowflake snowflake;

    private final ChatRoomSecurity chatRoomSecurity;

    public void sendMessageToChatroom(Long chatroomId, Long userId, ClientMessageDTO message) {

        chatRoomSecurity.ensureAccessToRoom(userId, chatroomId);

        MessageDTO trustedMessage = new MessageDTO(snowflake.generateId(), userId, chatroomId, message.content(),
                MessageType.GROUP,
                Instant.now());
        queueService.sendMessage(trustedMessage);
    }
}
