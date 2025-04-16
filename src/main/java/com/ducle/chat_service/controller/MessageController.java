package com.ducle.chat_service.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ducle.chat_service.model.dto.MessageDTO;
import com.ducle.chat_service.service.queue_service.QueueService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final QueueService queueService;
   
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send/{chatroomId}")
    public void sendMessageToChatroom(@DestinationVariable String chatroomId,@Valid MessageDTO message) {
        queueService.sendMessage(message);
        messagingTemplate.convertAndSend("/topic/chatRoom/" + chatroomId, message);
    }

}
