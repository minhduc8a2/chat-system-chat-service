package com.ducle.chat_service.security;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.ducle.chat_service.repository.ChatRoomMemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoomAccessChecker {

    private final ChatRoomMemberRepository repository;

    @Cacheable(value = "room-access", key = "#memberId + '-' + #chatroomId")
    public boolean hasAccess(Long memberId, Long chatroomId) {
        return repository.existsByChatRoomIdAndMemberId(chatroomId, memberId);
        
    }
}
