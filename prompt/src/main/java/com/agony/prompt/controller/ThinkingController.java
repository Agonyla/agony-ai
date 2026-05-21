package com.agony.prompt.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/21 15:23
 * @describe:
 */
@RestController
@RequestMapping("/api/thinking")
public class ThinkingController {

    // private final DashScopeChatModel chatModel;
    //
    // public ThinkingController(DashScopeChatModel chatModel) {
    //     this.chatModel = chatModel;
    // }
    //
    // @GetMapping("/qwen3")
    // public String deepAnalysis(@RequestParam String question) {
    //     return chatModel.call(new Prompt(
    //             new UserMessage(question),
    //             DashScopeChatOptions.builder()
    //                     .withModel("qwen3-235b-a22b")  // Qwen3 支持思考模式的模型
    //                     .withEnableThinking(true)       // 开启内置思考模式
    //                     .build()
    //     )).getResult().getOutput().getText();
    // }

    private final ChatClient chatClient;

    public ThinkingController(DashScopeChatModel dashScopeChatModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
    }

    @GetMapping
    public String deepAnalyze(@RequestParam String question) {

        return chatClient.prompt()
                .user(question)
                .options(DashScopeChatOptions.builder()
                        .model("qwen-plus")
                        .enableThinking(true)
                        .build())
                .call()
                .content();
    }
}