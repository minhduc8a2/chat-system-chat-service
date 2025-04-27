package com.ducle.chat_service.model.enums;

public enum ChatRoomSortField {
    id, name, type, status, createdAt;

    public static ChatRoomSortField fromString(String value) {
        try {
            return ChatRoomSortField.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid sort field. Allowed values: id, name, type, status, createdAt");
        }
    }
}
