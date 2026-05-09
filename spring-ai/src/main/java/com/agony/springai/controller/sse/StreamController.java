package com.agony.springai.controller.sse;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author: Agony
 * @create: 2026/5/8 19:14
 * @describe: 流式对话
 */
@RestController
@RequestMapping("/api/stream")
public class StreamController {

    private final ChatClient chatClient;

    public StreamController(ChatClient.Builder builder) {
        this.chatClient = builder.defaultSystem("你是一个Java助手").build();
    }

    /**
     * 流式对话接口
     * produces = TEXT_EVENT_STREAM_VALUE 告诉 Spring 这是 SSE 响应
     *
     * @param message message
     * @return Flux<String>
     */
    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam String message) {

        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }
}