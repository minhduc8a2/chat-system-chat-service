package com.ducle.chat_service.service;

import java.time.Instant;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.exception.AccessDeniedException;
import com.ducle.chat_service.model.dto.ClientMessageDTO;
import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.model.enums.MessageType;
import com.ducle.chat_service.security.ChatRoomSecurity;
import com.ducle.chat_service.service.queue_service.QueueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final QueueService queueService;

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomSecurity chatRoomSecurity;

    public void sendMessageToChatroom(Long chatroomId, ClientMessageDTO message,
            Message<?> fullMessage) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(fullMessage);
        @SuppressWarnings("null")
        Long userId = (Long) accessor.getSessionAttributes().get("userId");
        if (!chatRoomSecurity.hasAccessToRoom(userId, chatroomId)) {
            throw new AccessDeniedException("User not allowed to chat in this room");
        }

        MessageDTO trustedMessage = new MessageDTO(userId, chatroomId, message.content(), MessageType.GROUP,
                Instant.now());
        queueService.sendMessage(trustedMessage);
        messagingTemplate.convertAndSend("/topic/chat_room/" + chatroomId, message);
    }
}
