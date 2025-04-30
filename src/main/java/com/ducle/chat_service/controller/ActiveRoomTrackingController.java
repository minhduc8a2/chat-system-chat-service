package com.ducle.chat_service.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.ducle.chat_service.service.ActiveRoomTrackingService;
import com.ducle.chat_service.util.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ActiveRoomTrackingController {
    private final ActiveRoomTrackingService activeRoomTrackingService;

    @MessageMapping("/unsubcribe/chat_rooms/{chatroomId}")
    public void sendMessageToChatroom(@DestinationVariable Long chatroomId, SimpMessageHeaderAccessor headerAccessor) {
        Long userId = SessionUtils.getUserIdFromSession(headerAccessor);
        Long activeChatroomId = activeRoomTrackingService.getActiveRoom(userId);
        if (activeChatroomId.equals(chatroomId)) {
            activeRoomTrackingService.clearActiveRoom(userId);
        }
        activeRoomTrackingService.updateLastSeenTimeStamp(chatroomId, userId);

    }
}
