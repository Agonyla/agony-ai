package com.agony.prompt.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/21 14:14
 * @describe: 固定 System + 动态 System 补充（临时覆盖场景）
 */
@RestController
@RequestMapping("/api/translate")
public class TranslateController {

    public static final String BASE_SYSTEM = "你是一个技术助手，回答简洁准确。";

    private final ChatClient chatClient;

    public TranslateController(@Qualifier("techAssistantClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    public String translate(@RequestParam String text, @RequestParam String language) {

        return chatClient.prompt()
                .system(BASE_SYSTEM + "\n此外：你是专业翻译，只做翻译，不解释。")
                .user(text)
                .call()
                .content();
    }
}