package com.ducle.chat_service.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.ducle.chat_service.model.entity.ChatRoomMember;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);
}
