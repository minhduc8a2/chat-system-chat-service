package com.ducle.chat_service.model.dto;

import org.springframework.http.HttpStatus;

public record ErrorDTO(
        HttpStatus statusCode,
        String message) {

}
