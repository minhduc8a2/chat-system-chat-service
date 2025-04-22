package com.ducle.chat_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ducle.chat_service.model.dto.ChatRoomDTO;
import com.ducle.chat_service.service.ChatRoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("${api.chat-rooms}")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<Void> createChatRoom(@RequestHeader("X-User-UserId") Long userId,
            @RequestBody @Valid ChatRoomDTO chatRoomDTO) {
        return ResponseEntity.created(chatRoomService.createChatRoom(userId, chatRoomDTO)).build();
    }

}
