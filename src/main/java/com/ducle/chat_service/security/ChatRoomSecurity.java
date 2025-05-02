package com.ducle.chat_service.security;

import org.springframework.stereotype.Component;

import com.ducle.chat_service.exception.AccessDeniedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatRoomSecurity {
    private final RoomAccessChecker checker;

    public void ensureAccessToRoom(Long userId, Long chatroomId) {
        boolean check = checker.hasAccess(userId, chatroomId);
        log.info("Check : " + check);
        if (!check) {
            throw new AccessDeniedException("User not allowed to chat in this room");
        }

    }

}
