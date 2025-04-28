package com.ducle.chat_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ducle.chat_service.model.dto.ChatRoomDTO;
import com.ducle.chat_service.service.ChatRoomService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping
    public ResponseEntity<Page<ChatRoomDTO>> getChatRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(chatRoomService.getChatRooms(page, size, sortBy, sortDir));
    }

    @GetMapping("/user")
    public ResponseEntity<Page<ChatRoomDTO>> getChatRooms(
            @RequestHeader("X-User-UserId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(chatRoomService.getChatRoomsForUser(userId, page, size, sortBy, sortDir));
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<Void> joinRoom(@RequestHeader("X-User-UserId") Long userId, @PathVariable Long id) {
        chatRoomService.joinRoom(userId, id);
        return ResponseEntity.ok(null);
    }

}
