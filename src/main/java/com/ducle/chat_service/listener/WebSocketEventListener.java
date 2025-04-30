package com.ducle.chat_service.listener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.ducle.chat_service.service.ActiveRoomTrackingService;
import com.ducle.chat_service.service.PresenceService;
import com.ducle.chat_service.service.WebSocketSessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final WebSocketSessionService webSocketSessionService;
    private final ActiveRoomTrackingService activeRoomTrackingService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // tasks
        webSocketSessionService.handleConnect(headerAccessor);

        // log minimal info here
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        if (userId != null) {
            log.info("User connected: " + userId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        webSocketSessionService.handleDisconnect(headerAccessor);

        // log minimal info here
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        if (userId != null) {
            log.info("User disconnected: " + userId);
            Long chatRoomId = activeRoomTrackingService.getActiveRoom(userId);

            if (chatRoomId != null) {
                activeRoomTrackingService.clearActiveRoom(userId);
                activeRoomTrackingService.updateLastSeenTimeStamp(chatRoomId, userId);
            }
        }

    }
}
