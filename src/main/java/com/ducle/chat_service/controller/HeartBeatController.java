package com.ducle.chat_service.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.ducle.chat_service.service.PresenceService;
import com.ducle.chat_service.util.SessionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HeartBeatController {

    private final PresenceService presenceService;

    @MessageMapping("/heartbeat")
    @SendToUser("/queue/heartbeatReply")
    public String receiveHeartbeat(SimpMessageHeaderAccessor headerAccessor) {
        Long userId = SessionUtils.getUserIdFromSession(headerAccessor);
        if (!presenceService.getUserOnlineStatus(userId)) {
            presenceService.sendOnlineStatus(userId, true);
        }
        presenceService.setUserOnline(userId);
        return "pong";
    }
}
