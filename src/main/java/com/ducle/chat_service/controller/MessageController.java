package com.ducle.chat_service.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.ducle.chat_service.exception.AccessDeniedException;
import com.ducle.chat_service.exception.WebSocketExceptionHandler;
import com.ducle.chat_service.model.dto.ClientMessageDTO;
import com.ducle.chat_service.model.dto.ErrorDTO;
import com.ducle.chat_service.service.MessageService;
import com.ducle.chat_service.util.SessionUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;
    private final WebSocketExceptionHandler exceptionHandler;

    @MessageMapping("/chat.send/{chatroomId}")
    public void sendMessageToChatroom(@DestinationVariable Long chatroomId, @Valid ClientMessageDTO message,
            SimpMessageHeaderAccessor accessor) {
        Long userId = SessionUtils.getUserIdFromSession(accessor);
        messageService.sendMessageToChatroom(chatroomId, userId, message);
    }

    @MessageExceptionHandler(AccessDeniedException.class)
    @SendToUser("/queue/errors")
    public ErrorDTO handleAccessDenied(AccessDeniedException ex) {
        return exceptionHandler.handleAccessDeniedException(ex);
    }

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public ErrorDTO handleAny(Exception ex) {
        return exceptionHandler.handleGeneralException(ex);
    }

}
