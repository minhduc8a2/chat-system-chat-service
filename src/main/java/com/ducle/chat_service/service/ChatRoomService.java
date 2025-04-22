package com.ducle.chat_service.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.ducle.chat_service.mapper.ChatRoomMapper;
import com.ducle.chat_service.model.dto.ChatRoomDTO;
import com.ducle.chat_service.model.entity.ChatRoom;
import com.ducle.chat_service.model.entity.ChatRoomMember;
import com.ducle.chat_service.model.enums.ChatRoomMemberRole;
import com.ducle.chat_service.repository.ChatRoomMemberRepository;
import com.ducle.chat_service.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    @Value("${api.chat-rooms}")
    private String chatRoomApiUrl;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    public URI createChatRoom(Long userId, ChatRoomDTO chatRoomDTO) {
        ChatRoom chatRoom = chatRoomMapper.toChatRoom(chatRoomDTO);
        chatRoom.setOwnerId(userId);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomMember member = new ChatRoomMember(null, savedChatRoom, userId,ChatRoomMemberRole.MANAGER);
        chatRoomMemberRepository.save(member);

        return UriComponentsBuilder.fromUriString(chatRoomApiUrl+"/{id}").buildAndExpand(savedChatRoom.getId()).toUri();
    }
}
