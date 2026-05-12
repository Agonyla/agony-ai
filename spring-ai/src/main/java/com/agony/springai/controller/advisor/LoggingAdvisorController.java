package com.agony.springai.controller.advisor;

import com.agony.springai.advisor.LoggingAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/12 13:24
 * @describe:
 */
@RestController
@RequestMapping("/api/logging-advisor")
public class LoggingAdvisorController {

    private final ChatClient chatClient;

    public LoggingAdvisorController(ChatClient.Builder builder, LoggingAdvisor loggingAdvisor) {
        this.chatClient = builder.defaultSystem("你是一个 Java 技术助手")
                .defaultAdvisors(loggingAdvisor)
                .build();
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}