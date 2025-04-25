package com.ducle.chat_service.model.dto;

import java.time.Instant;
import com.ducle.chat_service.model.enums.ChatRoomStatus;
import com.ducle.chat_service.model.enums.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatRoomDTO(
        Long id,
        @NotBlank String name,
        String description,
        @NotNull ChatRoomType type,
        ChatRoomStatus status,
        Long ownerId,
        Instant createdAt,
        Instant updatedAt

) {

}
