package com.ducle.chat_service.controller;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
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
        presenceService.setUserOnline(userId);
        return "pong";
    }
}
