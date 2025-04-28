package com.ducle.chat_service.model.dto;

import com.ducle.chat_service.model.enums.CommandType;

public record CommandDTO(
        Long chatRoomId,
        CommandType type,
        String payload) {

}
