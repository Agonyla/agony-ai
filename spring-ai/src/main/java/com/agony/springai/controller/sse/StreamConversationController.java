package com.agony.springai.controller.sse;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author: Agony
 * @create: 2026/5/9 10:53
 * @describe: 带 conversationId 的流式多轮对话
 */
@RestController
@RequestMapping("/api/stream/conversation")
public class StreamConversationController {

    private final ChatClient chatClient;

    // 单独持有 chatMemory，每次请求按 conversationId 构建 Advisor
    private final MessageWindowChatMemory chatMemory;

    public StreamConversationController(ChatClient.Builder builder) {

        this.chatMemory = MessageWindowChatMemory.builder().maxMessages(10).build();
        this.chatClient = builder.defaultSystem("你是一个Java助手").build();
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(
            @RequestParam String message,
            @RequestParam(defaultValue = "default") String conversationId) {

        return chatClient.prompt()
                .user(message)
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).conversationId(conversationId).build())
                .stream()
                .content();
    }
}