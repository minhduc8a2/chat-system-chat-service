package com.ducle.chat_service.service;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketSessionService {

    private final PresenceService presenceService;

    public void handleConnect(StompHeaderAccessor headerAccessor) {
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");

        if (userId != null) {
            presenceService.setUserOnline(userId);
            presenceService.sendOnlineStatus(userId, true);
        }
    }


    public void handleDisconnect(StompHeaderAccessor headerAccessor) {
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");

        
    }
}
