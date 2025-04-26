package com.ducle.chat_service.controller;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.ducle.chat_service.util.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HeartBeatController {
    @Value("${server.id}")
    private String serverId;

    @Value("${presence.redis-key-format}")
    private String keyFormat;

    private final StringRedisTemplate redisTemplate;

    @MessageMapping("/heartbeat")
    public void receiveHeartbeat(Message<?> message) {

        Long userId = SessionUtils.getUserIdFromSession(message);
        String key = String.format(keyFormat, serverId, userId);
        log.info("Heartbeat----------------------------------");
        log.info(key);

        redisTemplate.opsForValue().set(key, "online", 15, TimeUnit.SECONDS);
    }
}
