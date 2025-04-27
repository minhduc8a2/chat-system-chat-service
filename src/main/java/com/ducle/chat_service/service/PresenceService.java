package com.ducle.chat_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresenceService {
    private static final String ONLINE_STATUS_DESTIONATION = "/topic/presence";

    @Value("${server.id}")
    private String serverId;
    @Value("${presence.redis-key-format}")
    private String keyFormat;

    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendOnlineStatus(Long userId, boolean isOnline) {
        Map<String, Boolean> status = new HashMap<>();
        status.put("onlineStatus", isOnline);
        messagingTemplate.convertAndSend(ONLINE_STATUS_DESTIONATION + "/" + userId, status);
    }

    public Map<String, Object> sendOnlineStatus(Long userId) {
        String key = String.format(keyFormat, serverId, userId);
        Boolean isOnline = redisTemplate.hasKey(key);
        Map<String, Object> status = new HashMap<>();
        status.put("onlineStatus", isOnline);
        status.put("userId", userId);
        return status;
    }
}
