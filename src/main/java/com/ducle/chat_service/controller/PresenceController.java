package com.ducle.chat_service.controller;

import java.security.Principal;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ducle.chat_service.util.SessionUtils;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PresenceController {
    @Value("${server.id}")
    private String serverId;
    @Value("${presence.redis-key-format}")
    private String keyFormat;
    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/presence/{userId}")
    public void receiveHeartbeat(@DestinationVariable Long userId, Message<?> message) {
        Long senderId = SessionUtils.getUserIdFromSession(message);
        String key = String.format(keyFormat, serverId, userId);
        Boolean isOnline = redisTemplate.hasKey(key);
        String destination = "/queue/presence";

        messagingTemplate.convertAndSendToUser(
                senderId.toString(),
                destination,
                isOnline);
    }
}
