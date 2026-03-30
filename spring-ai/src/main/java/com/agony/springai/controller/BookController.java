package com.agony.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/3/30 14:38
 * @describe:
 */
@RestController
@RequestMapping("/book")
public class BookController {

    private final ChatClient chatClient;

    public BookController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    record BookRecommendation(String title, String author, int year, String genre, String summary) {
    }

    @GetMapping("/recommend")
    public List<BookRecommendation> getRecommendations() {

        return chatClient.prompt()
                .user("请给我推荐五本日本小说")
                .call()
                .entity(new ParameterizedTypeReference<List<BookRecommendation>>() {
                });
    }
}