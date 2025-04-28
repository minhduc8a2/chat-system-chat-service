package com.ducle.chat_service;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import com.ducle.chat_service.model.dto.ChatRoomDTO;
import com.ducle.chat_service.model.enums.ChatRoomType;
import com.ducle.chat_service.service.ChatRoomService;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class ChatServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatServiceApplication.class, args);
	}

	@Bean
    public ApplicationRunner initData(ChatRoomService chatRoomService) {
        return (args) -> {
            chatRoomService.createChatRoom(1L,
                    new ChatRoomDTO(null, "Sample Room 1", null, ChatRoomType.PUBLIC, null, 1L, null, null));

        };
    }

}
