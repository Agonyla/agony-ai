package com.agony.springaialibaba.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/18 18:15
 * @describe:
 */
@Service
public class BadApproachService {

    private final ChatClient qwenChatClient;
    private final ChatClient kimiChatClient;

    public BadApproachService(
            @Qualifier("primaryChatClient") ChatClient qwenChatClient,
            @Qualifier("backupChatClient") ChatClient kimiChatClient) {

        this.qwenChatClient = qwenChatClient;
        this.kimiChatClient = kimiChatClient;
    }

    /**
     * 两个模型串行
     *
     * @param question
     * @return
     */
    public Map<String, String> serialChat(String question) {

        // 串行调用：总耗时 = A耗时 + B耗时，两个请求完全没有依赖，却白白等了一倍
        // 假设耗时 3s
        String kimiContent = kimiChatClient.prompt().user(question).call().content();

        // 假设耗时 2s
        String qwenContent = qwenChatClient.prompt().user(question).call().content();

        // 总耗时：5s，实际完全可以压缩到 3s
        return Map.of("qwen", qwenContent, "deepseek", kimiContent);
    }

}