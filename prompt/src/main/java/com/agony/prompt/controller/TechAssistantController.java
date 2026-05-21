package com.agony.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/21 14:13
 * @describe:
 */

@RestController
@RequestMapping("/api/tech")
public class TechAssistantController {

    private final ChatClient techAssistantClient;

    public TechAssistantController(ChatClient techAssistantClient) {
        this.techAssistantClient = techAssistantClient;
    }

    @GetMapping
    public String ask(@RequestParam String question) {
        return techAssistantClient.prompt()
                .user(question)
                .call()
                .content();
    }
}