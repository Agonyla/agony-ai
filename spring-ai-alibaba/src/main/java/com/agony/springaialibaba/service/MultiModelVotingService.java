package com.agony.springaialibaba.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/5/19 10:03
 * @describe:
 */
@Service
public class MultiModelVotingService {

    private final List<ChatClient> chatClients;
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public MultiModelVotingService(
            @Qualifier("primaryChatClient") ChatClient c1,
            @Qualifier("backupChatClient") ChatClient c2
    ) {
        this.chatClients = Arrays.asList(c1, c2);
    }

    public record VoteResult(String answer, int votes, double confidence) {
    }

    /**
     * 多模型投票，返回票数最多的答案和置信度
     *
     * @param question
     * @return
     * @throws Exception
     */
    public VoteResult vote(String question) throws Exception {

        List<CompletableFuture<String>> futures = chatClients.stream()
                .map(chatClient -> CompletableFuture.supplyAsync(
                        () -> chatClient.prompt()
                                .system("只回答 YES 或 NO，不要有其他内容")
                                .user(question)
                                .call()
                                .content()
                                .trim()
                                .toUpperCase()))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(30, TimeUnit.SECONDS);

        // 统计各答案的票数

        Map<String, Long> votes = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        return "ERROR";
                    }
                })
                .filter(r -> !r.equals("ERROR"))
                .collect(Collectors.groupingBy(r -> r, Collectors.counting()));

        String winner = votes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("UNKNOW");

        long winnerVotes = votes.getOrDefault(winner, 0L);
        double confidence = (double) winnerVotes / chatClients.size();

        return new VoteResult(winner, (int) winnerVotes, confidence);
    }

}