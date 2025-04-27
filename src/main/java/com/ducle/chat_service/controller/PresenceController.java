package com.ducle.chat_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.ducle.chat_service.service.PresenceService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PresenceController {
    @Value("${server.id}")
    private String serverId;
    @Value("${presence.redis-key-format}")
    private String keyFormat;

    private final PresenceService presenceService;

    @MessageMapping("/presence/{userId}")
    @SendToUser("/queue/presence/others")
    public Map<String, Object> sendUserOnlineStatus(@DestinationVariable Long userId, Message<?> message) {
        return presenceService.sendOnlineStatus(userId);
    }
}
