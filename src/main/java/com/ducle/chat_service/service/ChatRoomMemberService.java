package com.ducle.chat_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.model.dto.BasicUserInfoDTO;
import com.ducle.chat_service.model.enums.ChatRoomMemberSortField;
import com.ducle.chat_service.repository.ChatRoomMemberRepository;
import com.ducle.chat_service.security.ChatRoomSecurity;
import com.ducle.chat_service.util.PaginationHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomMemberService {
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomSecurity chatRoomSecurity;
    private final BasicUserInfoService basicUserInfoService;
    private final PresenceService presenceService;

    public Page<BasicUserInfoDTO> getChatRoomMembers(Long chatRoomId, Long userId, int page, int size, String sortBy,
            String sortDir) {
        chatRoomSecurity.ensureAccessToRoom(userId, chatRoomId);
        Pageable pageable = PaginationHelper.generatePageable(page, size, sortBy, sortDir,
                ChatRoomMemberSortField.class);

        var memberIdPage = chatRoomMemberRepository.findAllMemberIdByChatRoomId(chatRoomId, pageable);

        var memberIds = memberIdPage.getContent();
        var userInfoList = basicUserInfoService.getUsersByIds(memberIds);

        var userOnlineStatusMap = presenceService.getUsersOnlineStatus(memberIds);

        var onlineStatusUpdatedUserInfoList = userInfoList.stream()
                .map(userInfo -> new BasicUserInfoDTO(
                        userInfo.id(),
                        userInfo.username(),
                        userOnlineStatusMap.getOrDefault(userInfo.id(), false)))
                .toList();
        return new PageImpl<>(onlineStatusUpdatedUserInfoList, pageable, memberIdPage.getTotalElements());
    }

}
