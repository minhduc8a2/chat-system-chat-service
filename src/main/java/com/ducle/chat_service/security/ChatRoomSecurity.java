package com.ducle.chat_service.security;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.ducle.chat_service.exception.AccessDeniedException;
import com.ducle.chat_service.util.SessionUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatRoomSecurity {
    private final RoomAccessChecker checker;

    public void ensureAccessToRoom(Message<?> fullMessage, Long chatroomId) {

        Long userId = SessionUtils.getUserIdFromSession(fullMessage);
        if (!checker.hasAccess(userId, chatroomId)) {
            throw new AccessDeniedException("User not allowed to chat in this room");
        }

    }

    public void ensureToGetJoinedRooms(Long id, Long userId) {
        if (!id.equals(userId)) {
            throw new AccessDeniedException("User not allowed to chat in this room");
        }

    }

}
