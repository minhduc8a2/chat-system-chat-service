package com.ducle.chat_service.listener;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.ducle.chat_service.service.ActiveRoomTrackingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubcribeEventListener {
    private final ActiveRoomTrackingService activeRoomTrackingService;

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();

        if (destination != null && destination.startsWith("/topic/chat_room/")) {
            Long chatRoomId = Long.valueOf(destination.split("/")[3]);
            Object userIdAttr = headerAccessor.getSessionAttributes().get("userId");

            if (userIdAttr != null) {
                try {
                    Long userId = Long.parseLong(userIdAttr.toString());
                    activeRoomTrackingService.setActiveRoom(userId, chatRoomId);
                    log.info("User {} subscribed to room {}", userId, chatRoomId);
                } catch (NumberFormatException e) {
                    log.error("Invalid userId format in session: {}", userIdAttr);
                }
            } else {
                log.warn("No userId found in session attributes during subscription");
            }
        }

    }

}