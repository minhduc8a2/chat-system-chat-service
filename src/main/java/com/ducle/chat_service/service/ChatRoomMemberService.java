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

@Service
@RequiredArgsConstructor
public class ChatRoomMemberService {
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatRoomSecurity chatRoomSecurity;
    private final BasicUserInfoService basicUserInfoService;

    public Page<BasicUserInfoDTO> getChatRoomMembers(Long id, Long userId, int page, int size, String sortBy,
            String sortDir) {

        chatRoomSecurity.ensureToGetJoinedRooms(id, userId);
        Pageable pageable = PaginationHelper.generatePageable(page, size, sortBy, sortDir,
                ChatRoomMemberSortField.class);

        var memberIdPage = chatRoomMemberRepository.findAllMemberIdByChatRoomId(id, pageable);
        var userInfoList = basicUserInfoService.getUsersByIds(memberIdPage.getContent());

        return new PageImpl<>(userInfoList, pageable, memberIdPage.getTotalElements());

    }
}
