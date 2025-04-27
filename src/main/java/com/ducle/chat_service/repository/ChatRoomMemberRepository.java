package com.ducle.chat_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ducle.chat_service.model.entity.ChatRoomMember;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {

    boolean existsByChatRoomIdAndMemberId(Long chatRoomId, Long memberId);

    @Query("""
                SELECT crm.memberId FROM ChatRoomMember crm
                WHERE crm.chatRoom.id = :chatRoomId
            """)
    Page<Long> findAllMemberIdByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);
}
