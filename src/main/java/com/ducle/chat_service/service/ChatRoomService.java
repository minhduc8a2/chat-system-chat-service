package com.ducle.chat_service.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.ducle.chat_service.exception.EntityNotExistsException;
import com.ducle.chat_service.mapper.ChatRoomMapper;
import com.ducle.chat_service.model.dto.BasicUserInfoDTO;
import com.ducle.chat_service.model.dto.ChatRoomDTO;
import com.ducle.chat_service.model.entity.ChatRoom;
import com.ducle.chat_service.model.entity.ChatRoomMember;
import com.ducle.chat_service.model.enums.ChatRoomMemberRole;
import com.ducle.chat_service.model.enums.ChatRoomSortField;
import com.ducle.chat_service.model.enums.ChatRoomStatus;
import com.ducle.chat_service.model.enums.CommandType;
import com.ducle.chat_service.model.enums.MessageType;
import com.ducle.chat_service.repository.ChatRoomMemberRepository;
import com.ducle.chat_service.repository.ChatRoomRepository;
import com.ducle.chat_service.util.PaginationHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    @Value("${api.chat-rooms}")
    private String chatRoomApiUrl;

    private final ChatRoomRepository chatRoomRepository;

    private final ChatRoomMapper chatRoomMapper;

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    private final ObjectMapper objectMapper;

    private final MessageService messageService;

    private final BasicUserInfoService basicUserInfoService;

    private final DelayExecutorService delayExecutorService;

    private final PresenceService presenceService;

    public URI createChatRoom(Long userId, ChatRoomDTO chatRoomDTO) {
        ChatRoom chatRoom = chatRoomMapper.toChatRoom(chatRoomDTO);
        chatRoom.setOwnerId(userId);
        chatRoom.setStatus(ChatRoomStatus.ACTIVE);

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        ChatRoomMember member = new ChatRoomMember(null, savedChatRoom, userId, ChatRoomMemberRole.MANAGER, null);
        chatRoomMemberRepository.save(member);

        return UriComponentsBuilder.fromUriString(chatRoomApiUrl + "/{id}").buildAndExpand(savedChatRoom.getId())
                .toUri();
    }

    @CacheEvict(value = "room-access", key = "#userId + '-' + #chatroomId")
    public void joinRoom(Long userId, Long chatRoomId) {
        if (chatRoomMemberRepository.existsByChatRoomIdAndMemberId(chatRoomId, userId)) {
            throw new IllegalArgumentException("User is already a member of this chat room.");
        }
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotExistsException("Chat room not found"));

        ChatRoomMember newMember = new ChatRoomMember(chatRoom, userId, ChatRoomMemberRole.MEMBER);
        chatRoomMemberRepository.save(newMember);

        BasicUserInfoDTO userInfo = basicUserInfoService.getUserById(userId);

        // notify through websocket
        try {
            String payload = objectMapper.writeValueAsString(userInfo);
            messageService.sendCommandMessageToChatroom(payload, chatRoomId, CommandType.MEMBER_JOIN);
            delayExecutorService.executeAfterDelay(
                    () -> {
                        // send join message to chatroom
                        String content = "#".concat(userInfo.username()).concat(" has joined");
                        messageService.sendSystemMessageToChatroom(content, chatRoomId, MessageType.JOIN);
                        // send online status
                        presenceService.sendOnlineStatus(userId, presenceService.getUserOnlineStatus(userId));
                    }, 3);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public Page<ChatRoomDTO> getChatRooms(int page, int size, String sortBy, String sortDir) {

        Pageable pageable = PaginationHelper.generatePageable(page, size, sortBy, sortDir, ChatRoomSortField.class);
        return chatRoomRepository.findAll(pageable).map(chatRoomMapper::toChatRoomDTO);
    }

    public Page<ChatRoomDTO> getChatRoomsForUser(Long userId, int page, int size, String sortBy,
            String sortDir) {

        Pageable pageable = PaginationHelper.generatePageable(page, size, sortBy, sortDir, ChatRoomSortField.class);
        return chatRoomRepository.findByMemberId(userId, pageable).map(chatRoomMapper::toChatRoomDTO);
    }
}
