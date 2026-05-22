package com.agony.prompt.service;

import com.agony.prompt.entity.ContractRick;
import com.agony.prompt.enums.Verdict;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
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
 * @create: 2026/5/22 9:41
 * @describe:
 */
@Service
public class ContractAnalysisService {

    private final ChatClient chatClient;

    private final BeanOutputConverter<ContractRick> converter;

    public ContractAnalysisService(DashScopeChatModel dashScopeChatModel) {

        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
        this.converter = new BeanOutputConverter<>(ContractRick.class);
    }

    /**
     * 一致性分析
     *
     * @param clause
     * @return
     * @throws Exception
     */
    public ContractRick analyzeWithConsistency(String clause) throws Exception {

        int sampleCount = 5;

        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .temperature(0.5)
                .build();

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        String userContent = "分析这个合同条款：\n" + clause
                + "\n\n请先逐步分析，再给出结论。\n\n" + converter.getFormat();

        List<CompletableFuture<ContractRick>> futures = new ArrayList<>();
        for (int i = 0; i < sampleCount; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> {
                String content = chatClient.prompt()
                        .user(userContent)
                        .system("你是合同法律顾问，分析合同条款是否存在法律风险。先思考，再给出结论。")
                        .options(options)
                        .call()
                        .content();
                return converter.convert(content);
            }, executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).get(60, TimeUnit.SECONDS);

        List<ContractRick> list = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return aggregateResults(list);

    }

    /**
     * 聚合分析
     *
     * @param list
     * @return
     */
    private ContractRick aggregateResults(List<ContractRick> list) {

        long yesCount = list.stream().filter(c -> c.hasRisk() == Verdict.YES).count();
        long noCount = list.stream().filter(c -> c.hasRisk() == Verdict.NO).count();

        // 主要判决
        Verdict majorVerdict = yesCount >= noCount ? Verdict.YES : Verdict.NO;

        // 风险类型
        String riskType = list.stream()
                .filter(c -> c.hasRisk() == majorVerdict)
                .map(ContractRick::riskType)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("位置风险");

        // 严重程度
        double severity = list.stream()
                .filter(c -> c.hasRisk() == majorVerdict)
                .mapToInt(ContractRick::severity)
                .average()
                .orElse(5.0);

        return new ContractRick(majorVerdict, riskType, (int) Math.round(severity));
    }
}