package com.agony.springaialibaba.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author: Agony
 * @create: 2026/5/18 18:25
 * @describe:
 */
@Service
public class ParallelChatService {

    private final ChatClient qwenChatClient;
    private final ChatClient kimiChatClient;

    public ParallelChatService(
            @Qualifier("primaryChatClient") ChatClient qwenChatClient,
            @Qualifier("backupChatClient") ChatClient kimiChatClient) {

        this.qwenChatClient = qwenChatClient;
        this.kimiChatClient = kimiChatClient;
    }

    /**
     * 并行调用两个模型，总耗时 ≈ max(A耗时, B耗时)
     *
     * @param question
     * @return
     * @throws Exception
     */
    public Map<String, String> parallelChat(String question) throws Exception {

        // 两个请求同时发出，各自在独立线程里跑
        CompletableFuture<String> qwenFuture = CompletableFuture.supplyAsync(
                () -> qwenChatClient.prompt().user(question).call().content()
        );

        CompletableFuture<String> kimiFuture = CompletableFuture.supplyAsync(
                () -> kimiChatClient.prompt().user(question).call().content()
        );

        // allOf 等两个都完成，超时 30s 抛异常
        CompletableFuture.allOf(qwenFuture, kimiFuture).get(30, TimeUnit.SECONDS);

        // 总耗时：≈ 3s（取两个里最慢的）
        return Map.of(
                "qwen", qwenFuture.get(),
                "kimi", kimiFuture.get()
        );
    }

}