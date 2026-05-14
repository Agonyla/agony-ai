package com.agony.springaialibaba.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/14 9:18
 * @describe: 联网搜索与深度思考
 */
@RestController
@RequestMapping("/api/qwen/search")
public class SearchController {

    private final ChatClient chatClient;

    public SearchController(DashScopeChatModel dashScopeChatModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
    }

    @GetMapping
    public String search(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .options(DashScopeChatOptions.builder()
                        .model("qwen-max")
                        .enableSearch(true) // 开启联网搜索
                        .build())
                .call()
                .content();
    }

    @GetMapping("/think")
    public String think(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .options(DashScopeChatOptions.builder()
                        .model("qwen3-235b-a22b")
                        .enableThinking(true)
                        .thinkingBudget(2000)   // 思考过程最多用 2000 token
                        .build())
                .call()
                .content();
    }

}