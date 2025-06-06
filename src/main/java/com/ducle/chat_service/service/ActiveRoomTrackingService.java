package com.ducle.chat_service.service;

import java.time.Instant;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.ducle.chat_service.exception.EntityNotExistsException;
import com.ducle.chat_service.model.entity.ChatRoomMember;
import com.ducle.chat_service.repository.ChatRoomMemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActiveRoomTrackingService {
    private static final String ROOM_TRACKING_KEY_FORMAT = "active_room:user:%s";
    private final StringRedisTemplate redisTemplate;

    private final ChatRoomMemberService chatRoomMemberService;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    private String generateKey(Long userId) {
        return String.format(ROOM_TRACKING_KEY_FORMAT, userId);
    }

    public void setActiveRoom(Long userId, Long chatRoomId) {
        redisTemplate.opsForValue().set(generateKey(userId), chatRoomId.toString());
    }

    public Long getActiveRoom(Long userId) {
        String key = generateKey(userId);
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? Long.parseLong(value.toString()) : null;
    }

    public void clearActiveRoom(Long userId) {
        redisTemplate.delete(generateKey(userId));
    }

    public void updateLastSeenTimeStamp(Long chatRoomId, Long userId) {
        chatRoomMemberService.updateLastSeenTimeStamp(chatRoomId, userId, Instant.now());
    }

    public Instant getRoomLastSeenTimeStamp(Long chatRoomId, Long userId) {
        ChatRoomMember member = chatRoomMemberRepository.findByChatRoomIdAndMemberId(chatRoomId, userId)
                .orElseThrow(() -> new EntityNotExistsException("User not found"));
        return member.getLastSeen();
    }
}
