package com.ducle.chat_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ducle.chat_service.model.dto.BasicUserInfoDTO;
import com.ducle.chat_service.model.dto.ChatRoomMemberDTO;
import com.ducle.chat_service.service.ChatRoomMemberService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("${api.chat-rooms}")
@RequiredArgsConstructor
public class MemberController {
    private final ChatRoomMemberService chatRoomMemberService;



    @GetMapping("/{chatRoomId}/members")
    public ResponseEntity<Page<BasicUserInfoDTO>> getMethodName(
            @Min(1) @PathVariable Long chatRoomId,
            @Min(1) @RequestHeader("X-User-UserId") Long userId,
            @Min(0) @RequestParam(defaultValue = "0") int page,
            @Min(1) @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity
                .ok(chatRoomMemberService.getChatRoomMembers(chatRoomId, userId, page, size, sortBy, sortDir));
    }

}