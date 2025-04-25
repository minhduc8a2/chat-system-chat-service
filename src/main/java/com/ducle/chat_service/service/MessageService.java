package com.ducle.chat_service.service;

import java.time.Instant;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.model.dto.ClientMessageDTO;
import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.model.enums.MessageType;
import com.ducle.chat_service.security.ChatRoomSecurity;
import com.ducle.chat_service.service.id_generator.impl.Snowflake;
import com.ducle.chat_service.service.queue_service.QueueService;
import com.ducle.chat_service.util.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final QueueService queueService;
    private final Snowflake snowflake;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomSecurity chatRoomSecurity;

    public void sendMessageToChatroom(Long chatroomId, ClientMessageDTO message,
            Message<?> fullMessage) {

        chatRoomSecurity.ensureAccessToRoom(fullMessage, chatroomId);

        Long userId = SessionUtils.getUserIdFromSession(fullMessage);

        MessageDTO trustedMessage = new MessageDTO(snowflake.generateId(), userId, chatroomId, message.content(),
                MessageType.GROUP,
                Instant.now());
        messagingTemplate.convertAndSend("/topic/chat_room/" + chatroomId, trustedMessage);
        queueService.sendMessage(trustedMessage);
    }
}
