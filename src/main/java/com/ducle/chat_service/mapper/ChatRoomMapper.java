package com.ducle.chat_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ducle.chat_service.model.dto.ChatRoomDTO;
import com.ducle.chat_service.model.entity.ChatRoom;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {

    @Mapping(target = "users", ignore = true)
    ChatRoom toChatRoom(ChatRoomDTO chatRoomDTO);

    ChatRoomDTO toChatRoomDTO(ChatRoom chatRoom);
}
