package com.ducle.chat_service.listener;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.ducle.chat_service.service.PresenceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisExpirationListener {
    @Value("${presence.redis-key-format-for-extract-user-id}")
    private String keyExtractFormat;

    private final PresenceService presenceService;

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListener listener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listener, new PatternTopic("__keyevent@0__:expired"));
        return container;
    }

    @Bean
    public MessageListener keyExpirationListener() {
        return (message, pattern) -> {
            String key = message.toString();
            log.info("Key expired: " + key);
            if (key.startsWith("presence.")) {
                Long userId = Long.valueOf(extractUserId(key));
                presenceService.sendOnlineStatus(userId, false);
            }
        };
    }

    public static String extractUserId(String key) {
        String[] parts = key.split(":");
        if (parts.length == 3) {
            return parts[2];
        } else {
            throw new IllegalArgumentException("Invalid key format: " + key);
        }
    }

}
