package com.ducle.chat_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ducle.chat_service.model.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("""
                SELECT crm.chatRoom FROM ChatRoomMember crm
                WHERE crm.memberId = :memberId
            """)
    Page<ChatRoom> findByMemberId(@Param("memberId") Long memberId, Pageable pageable);
}
