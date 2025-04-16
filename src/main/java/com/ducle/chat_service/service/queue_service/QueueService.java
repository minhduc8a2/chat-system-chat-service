package com.ducle.chat_service.service.queue_service;

import com.ducle.chat_service.model.dto.MessageDTO;

public interface QueueService {
    void sendMessage(MessageDTO message); // Send a message to the queue

}
