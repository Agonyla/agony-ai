package com.agony.springaialibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author: Agony
 * @create: 2026/5/13 13:39
 * @describe:
 */
@RestController
@RequestMapping("/api/qwen")
public class QwenChatController {

    private final ChatClient chatClient;

    public QwenChatController(DashScopeChatModel dashScopeChatModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem("你是一个专业的 Java 技术助手")
                .build();
    }

    // 普通对话
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    // 流式输出
    @GetMapping(value = "/stream", produces = "text/event-stream")
    public Flux<String> stream(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content();
    }

    // 动态指定模型规格：正常用 qwen-turbo 省钱，重要任务临时换 qwen-max
    @GetMapping("/chat-with-model")
    public String chatWithModel(
            @RequestParam String message,
            @RequestParam(defaultValue = "qwen-turbo") String model) {

        return chatClient.prompt()
                .user(message)
                .options(DashScopeChatOptions.builder()
                        .withModel(model)
                        .withTemperature(0.7)
                        .withMaxToken(1000)
                        .build())
                .call()
                .content();
    }
}