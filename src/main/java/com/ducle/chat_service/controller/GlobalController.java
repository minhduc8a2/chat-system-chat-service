package com.ducle.chat_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("${api.chat-service}")
public class GlobalController {
    @GetMapping
    public String greet() {
        return "hello";
    }
    
}
