package com.ducle.chat_service.service;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ducle.chat_service.model.dto.BasicUserInfoDTO;


@FeignClient("${auth-service.name}")
public interface BasicUserInfoService {
    @GetMapping("/internal/auth/users/{id}")
    BasicUserInfoDTO getUserById(@PathVariable Long id);

    @PostMapping("/internal/auth/users/batch")
    List<BasicUserInfoDTO> getUsersByIds(@RequestBody List<Long> ids);
}
