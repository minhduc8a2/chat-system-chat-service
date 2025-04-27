package com.ducle.chat_service.controller;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.ducle.chat_service.util.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HeartBeatController {
    private static final long HEARTBEAT_TTL_SECONDS = 10;
    @Value("${server.id}")
    private String serverId;

    @Value("${presence.redis-key-format}")
    private String keyFormat;

    private final StringRedisTemplate redisTemplate;

    @MessageMapping("/heartbeat")
    @SendToUser("/queue/heartbeatReply")
    public String receiveHeartbeat(SimpMessageHeaderAccessor headerAccessor) {
        Long userId = SessionUtils.getUserIdFromSession(headerAccessor);
        String key = String.format(keyFormat, userId);
        log.info("Heartbeat----------------------------------");
        log.info(key);
        redisTemplate.opsForValue().set(key, serverId, HEARTBEAT_TTL_SECONDS, TimeUnit.SECONDS);
        return "pong";
    }
}
