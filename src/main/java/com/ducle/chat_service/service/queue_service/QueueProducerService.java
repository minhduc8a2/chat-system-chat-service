package com.ducle.chat_service.service.queue_service;

import com.ducle.chat_service.model.dto.MessageDTO;

public interface QueueProducerService {
    void sendMessage(MessageDTO message); 

}
