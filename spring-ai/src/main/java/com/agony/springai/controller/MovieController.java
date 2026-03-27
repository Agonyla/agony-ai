package com.agony.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/3/27 13:52
 * @describe:
 */
@RestController
@RequestMapping("/movie")
public class MovieController {

    private final ChatClient chatClient;

    public MovieController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

}