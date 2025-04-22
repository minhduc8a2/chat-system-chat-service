package com.ducle.chat_service.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.service.queue_service.QueueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final QueueService queueService;

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send/{chatroomId}")
    public void sendMessageToChatroom(@DestinationVariable String chatroomId, @Valid MessageDTO message,
            Message<?> fullMessage) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(fullMessage);
        String username = (String) accessor.getSessionAttributes().get("username");
        log.info(username + " send message to chatroom " + chatroomId + ": " + message.content());
        // if (!userHasAccessToRoom(username, chatroomId)) {
        //     throw new AccessDeniedException("User not allowed to chat in this room");
        // }
        queueService.sendMessage(message);
        messagingTemplate.convertAndSend("/topic/chatRoom/" + chatroomId, message);
    }

}
