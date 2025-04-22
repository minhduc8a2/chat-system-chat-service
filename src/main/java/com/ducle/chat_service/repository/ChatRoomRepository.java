package com.ducle.chat_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ducle.chat_service.model.entity.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
