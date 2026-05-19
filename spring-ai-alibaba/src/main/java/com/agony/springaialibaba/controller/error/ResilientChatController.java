package com.agony.springaialibaba.controller.error;

import com.agony.springaialibaba.service.ResilientChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/19 18:04
 * @describe:
 */
@RestController
@RequestMapping("/api/resilient")
public class ResilientChatController {

    private final ResilientChatService resilientChatService;

    public ResilientChatController(ResilientChatService resilientChatService) {
        this.resilientChatService = resilientChatService;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return resilientChatService.chat(message);
    }
}