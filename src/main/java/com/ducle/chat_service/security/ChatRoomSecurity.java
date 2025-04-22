package com.ducle.chat_service.security;

import org.springframework.stereotype.Component;

import com.ducle.chat_service.repository.ChatRoomMemberRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatRoomSecurity {
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public boolean hasAccessToRoom(Long memberId, Long chatroomId) {
        return chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatroomId, memberId);
    }
}
