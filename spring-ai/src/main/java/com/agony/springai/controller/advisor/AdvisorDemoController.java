package com.agony.springai.controller.advisor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/12 10:59
 * @describe:
 */
@RestController
@RequestMapping("/api/advidor-demo")
public class AdvisorDemoController {

    private final ChatClient chatClient;

    private final MessageWindowChatMemory chatMemory;

    public AdvisorDemoController(ChatClient.Builder builder) {
        this.chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();
        this.chatClient = builder.defaultSystem("你是一个Java助手")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .build();
    }

    @GetMapping
    public String chat(
            @RequestParam String message,
            @RequestParam(defaultValue = "default") String conversationId) {

        return chatClient.prompt()
                .user(message)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).conversationId(conversationId).build())
                .call()
                .content();
    }
}