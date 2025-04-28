package com.ducle.chat_service.service;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketSessionService {

    private final PresenceService presenceService;

    public void handleConnect(StompHeaderAccessor headerAccessor) {
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");

        if (userId != null) {
            presenceService.setUserOnline(userId);
            presenceService.sendOnlineStatus(userId, true);

            presenceService.setWebsocketConnectionStatus(userId, true);

        }
    }

    public void handleDisconnect(StompHeaderAccessor headerAccessor) {
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        presenceService.setWebsocketConnectionStatus(userId, false);

    }
}
