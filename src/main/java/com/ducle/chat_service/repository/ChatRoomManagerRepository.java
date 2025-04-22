package com.ducle.chat_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ducle.chat_service.model.entity.ChatRoomManager;

public interface ChatRoomManagerRepository extends JpaRepository<ChatRoomManager, Long> {

    List<ChatRoomManager> findByChatRoomId(Long chatRoomId);

    boolean existsByChatRoomIdAndManagerId(Long chatRoomId, Long managerId);

}
