package com.agony.prompt.service;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/5/21 16:03
 * @describe:
 */
@Service
public class SelfConsistencyService {

    private final ChatClient chatClient;

    public SelfConsistencyService(DashScopeChatModel dashScopeChatModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
    }

    public String query(String question, int sampleCount) throws Exception {

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < sampleCount; i++) {

            futures.add(CompletableFuture.supplyAsync(() ->
                            chatClient.prompt()
                                    .user(question + "\n\n 让我们一步一步思考")
                                    .options(DashScopeChatOptions.builder()
                                            .temperature(0.7)
                                            .build())
                                    .call()
                                    .content()
                    , executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(60, TimeUnit.SECONDS);

        List<String> answers = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return majorityVote(answers);
    }

    public String majorityVote(List<String> answers) {

        return answers.stream()
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(answers.get(0));

    }
}