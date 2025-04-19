package com.ducle.chat_service.model.dto;

import java.io.Serializable;
import java.time.Instant;

import com.ducle.chat_service.model.enums.MessageType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageDTO(

        Long senderId,
        @NotNull @Min(1) Long roomId,
        @NotBlank String content,
        @NotNull MessageType type,
        Instant timestamp

) implements Serializable {

    public MessageDTO(String message) {
        this(null, null, message, MessageType.SYSTEM, Instant.now());
    }

}
