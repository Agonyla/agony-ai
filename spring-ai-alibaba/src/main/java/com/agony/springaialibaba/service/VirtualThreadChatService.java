package com.agony.springaialibaba.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author: Agony
 * @create: 2026/5/19 9:12
 * @describe:
 */
@Service
public class VirtualThreadChatService {

    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    private final ChatClient qwenChatClient;
    private final ChatClient kimiChatClient;

    public VirtualThreadChatService(
            @Qualifier("primaryChatClient") ChatClient qwenChatClient,
            @Qualifier("backupChatClient") ChatClient kimiChatClient) {

        this.qwenChatClient = qwenChatClient;
        this.kimiChatClient = kimiChatClient;
    }

    /**
     * 赛马模式：同时向多个模型提问，任何一个返回就立刻响应，其余取消
     * 适合对延迟敏感、不要求特定模型的场景
     *
     * @param question
     * @return
     * @throws Exception
     */
    public String fastestResponse(String question) throws Exception {

        List<Callable<String>> tasks = List.of(
                () -> qwenChatClient.prompt().user(question).call().content(),
                () -> kimiChatClient.prompt().user(question).call().content()
        );

        return executor.invokeAny(tasks, 30, TimeUnit.SECONDS);
    }

    /**
     * 全量模式：所有模型都跑完，把结果一起返回
     *
     * @param question
     * @return
     * @throws Exception
     */
    public Map<String, String> allResponse(String question) throws Exception {

        CompletableFuture<String> qwenContent = CompletableFuture.supplyAsync(
                () -> qwenChatClient.prompt().user(question).call().content(), executor);

        CompletableFuture<String> kimiContent = CompletableFuture.supplyAsync(
                () -> kimiChatClient.prompt().user(question).call().content(), executor);

        CompletableFuture.allOf(qwenContent, kimiContent).get(30, TimeUnit.SECONDS);
        return Map.of(
                "qwen", qwenContent.get(),
                "kimi", kimiContent.get()
        );
    }
}