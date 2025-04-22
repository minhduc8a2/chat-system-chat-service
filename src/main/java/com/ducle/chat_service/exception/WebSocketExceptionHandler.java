package com.ducle.chat_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ducle.chat_service.model.dto.ErrorDTO;

@Component
public class WebSocketExceptionHandler {

    public ErrorDTO handleAccessDeniedException(AccessDeniedException ex) {
        return new ErrorDTO(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    public ErrorDTO handleGeneralException(Exception ex) {
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }
}

