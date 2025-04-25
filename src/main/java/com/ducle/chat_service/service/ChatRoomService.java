package com.ducle.chat_service.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.ducle.chat_service.mapper.ChatRoomMapper;
import com.ducle.chat_service.model.dto.ChatRoomDTO;
import com.ducle.chat_service.model.entity.ChatRoom;
import com.ducle.chat_service.model.entity.ChatRoomMember;
import com.ducle.chat_service.model.enums.ChatRoomMemberRole;
import com.ducle.chat_service.model.enums.ChatRoomSortField;
import com.ducle.chat_service.model.enums.ChatRoomStatus;
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
        chatRoom.setStatus(ChatRoomStatus.ACTIVE);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomMember member = new ChatRoomMember(null, savedChatRoom, userId, ChatRoomMemberRole.MANAGER);
        chatRoomMemberRepository.save(member);

        return UriComponentsBuilder.fromUriString(chatRoomApiUrl + "/{id}").buildAndExpand(savedChatRoom.getId())
                .toUri();
    }

    public Page<ChatRoomDTO> getChatRooms(int page, int size, String sortBy, String sortDir) {
        String[] fields = sortBy.split(",");
        String[] directions = sortDir.split(",");

        if (fields.length != directions.length) {
            throw new IllegalArgumentException("sortBy and sortDir must have the same number of elements.");
        }

        List<Sort.Order> orders = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            ChatRoomSortField sortField;
            try {
                sortField = ChatRoomSortField.valueOf(fields[i]); // validate each field
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid sortBy value: " + fields[i]);
            }

            Sort.Direction direction = directions[i].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            orders.add(new Sort.Order(direction, sortField.name()));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        return chatRoomRepository.findAll(pageable).map(chatRoomMapper::toChatRoomDTO);
    }
}
