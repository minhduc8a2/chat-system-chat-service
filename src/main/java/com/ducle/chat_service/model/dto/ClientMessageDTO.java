package com.ducle.chat_service.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientMessageDTO(@NotBlank String content) {

}
