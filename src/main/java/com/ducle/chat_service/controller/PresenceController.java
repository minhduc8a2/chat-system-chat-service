package com.ducle.chat_service.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ducle.chat_service.model.dto.UserPresenceDTO;
import com.ducle.chat_service.service.PresenceService;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PresenceController {

    private final PresenceService presenceService;

    @PostMapping("/internal/chat/chat-rooms/{chatRoomId}/presence/websocket/users/batch")
    public List<UserPresenceDTO> getWebsocketConnectionStatus(
            @PathVariable @Min(1) Long chatRoomId,
            @Min(0) @RequestParam(defaultValue = "0") int page,
            @Min(1) @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return presenceService.getChatRoomWebsocketConnectionStatusList(chatRoomId, page, size, sortBy,
                sortDir);
    }

}
