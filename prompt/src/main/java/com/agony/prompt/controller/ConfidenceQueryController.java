package com.agony.prompt.controller;

import com.agony.prompt.entity.AnswerWithConfidence;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/5/22 10:49
 * @describe:
 */
@RestController
@RequestMapping("/confidence")
public class ConfidenceQueryController {

    private final ChatClient chatClient;

    public ConfidenceQueryController(DashScopeChatModel dashScopeChatModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
    }

    @PostMapping("/analyze")
    public AnswerWithConfidence analyze(
            @RequestParam String question,
            @RequestParam(defaultValue = "5") int sampleCount) throws Exception {

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .temperature(0.7)
                .build();

        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (int i = 0; i < sampleCount; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> chatClient.prompt()
                    .user(question + "\n只输出最终答案，不要解释。")
                    .options(options)
                    .call()
                    .content(), executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(30, TimeUnit.SECONDS);

        List<String> results = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .toList();

        Map.Entry<String, Long> top = results.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow();

        double confidence = (double) top.getValue() / results.size();

        return new AnswerWithConfidence(top.getKey(), confidence, sampleCount);
    }
}