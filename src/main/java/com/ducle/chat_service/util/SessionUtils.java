package com.ducle.chat_service.util;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public class SessionUtils {
    private SessionUtils() {
    }

    public static Long getUserIdFromSession(Message<?> message) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);
        return (Long) accessor.getSessionAttributes().get("userId");
    }
}
