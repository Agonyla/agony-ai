package com.agony.springaialibaba.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * @author: Agony
 * @create: 2026/5/19 11:19
 * @describe:
 */
@Service
public class BatchAnalysisService {

    private final ChatClient chatClient;

    public BatchAnalysisService(@Qualifier("primaryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public enum Sentiment {POSITIVE, NEGATIVE, NEUTRAL}

    public record AnalysisResult(String text, Sentiment sentiment, int score) {
    }

    public record SentimentResult(Sentiment sentiment, int score) {
    }

    public static final Logger log = LoggerFactory.getLogger(BatchAnalysisService.class);

    /**
     * 并行批量情感分析
     *
     * @param texts       要分析的文本列表
     * @param concurrency 并发数，建议 5-10，太高容易触发模型 API 限流
     */
    public List<AnalysisResult> batchAnalyze(List<String> texts, int concurrency) {

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        // Semaphore 控制同时运行的任务数，permits 用完后新任务阻塞等待
        Semaphore semaphore = new Semaphore(concurrency);

        List<CompletableFuture<AnalysisResult>> futures = texts.stream()
                .map(text -> CompletableFuture.supplyAsync(() -> {
                    try {
                        semaphore.acquire();

                        try {
                            SentimentResult result = chatClient.prompt()
                                    .system("""
                                            分析文本情感，严格按以下 JSON 格式返回，不要有其他内容：
                                            {"sentiment":"POSITIVE","score":8}
                                            sentiment 只能是 POSITIVE / NEGATIVE / NEUTRAL，score 是 1-10 的整数，10 最正面。""")
                                    .user(text)
                                    .call()
                                    .entity(SentimentResult.class);

                            return new AnalysisResult(text, result.sentiment(), result.score());
                        } finally {
                            semaphore.release();
                        }

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }, executor))
                .toList();

        // 收集所有结果，单条失败记日志但不影响整体
        return futures.stream().
                map(future -> {
                    try {
                        return future.get(60, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error("批量分析单条失败：{}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

}