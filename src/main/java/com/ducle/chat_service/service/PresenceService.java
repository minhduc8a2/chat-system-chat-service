package com.ducle.chat_service.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.model.dto.UserPresenceDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService {
    private static final long HEARTBEAT_TTL_SECONDS = 10;
    private static final String ONLINE_STATUS_DESTIONATION = "/topic/presence";

    @Value("${server.id}")
    private String serverId;

    @Value("${presence.redis-key-format}")
    private String keyFormat;

    @Value("${websocket-connection-status.redis-key-format}")
    private String websocketConnectionKeyFormat;

    private final StringRedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public void sendOnlineStatus(Long userId, boolean isOnline) {
        Map<String, Object> status = new HashMap<>();
        status.put("isOnline", isOnline);
        status.put("userId", userId);
        messagingTemplate.convertAndSend(ONLINE_STATUS_DESTIONATION + "/" + userId, status);
    }

    public Boolean getUserOnlineStatus(Long userId) {
        String key = String.format(keyFormat, userId);
        return redisTemplate.hasKey(key);
    }

    public Map<Long, Boolean> getUsersOnlineStatus(List<Long> userIds) {

        List<String> keys = userIds.stream()
                .map(userId -> String.format(keyFormat, userId))
                .toList();

        List<String> results = redisTemplate.opsForValue().multiGet(keys);

        if (results == null) {
            results = Collections.emptyList();
        }

        Map<Long, Boolean> onlineStatusMap = new HashMap<>();

        for (int i = 0; i < userIds.size(); i++) {
            Long userId = userIds.get(i);
            String presence = results.get(i);
            onlineStatusMap.put(userId, presence != null);
        }

        return onlineStatusMap;
    }

    public void setUserOnline(Long userId) {
        String key = String.format(keyFormat, userId);
        log.info("Heartbeat----------------------------------");
        log.info(key);
        redisTemplate.opsForValue().set(key, serverId, HEARTBEAT_TTL_SECONDS, TimeUnit.SECONDS);
    }



    public void setWebsocketConnectionStatus(Long userId, boolean isConnected) {
        String key = String.format(websocketConnectionKeyFormat, userId);

        if (isConnected) {
            log.info("Websocket connection established: {}", key);
            redisTemplate.opsForValue().set(key, serverId);
        } else {
            redisTemplate.delete(key);
            log.info("Websocket disconnection: {}", key);

        }
    }


    public List<UserPresenceDTO> getChatRoomWebsocketConnectionStatusList(Long chatRoomId){
        return
    }

}
