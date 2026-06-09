package com.agony.agent.service;

import com.agony.agent.tools.RollingMemoryCompressor;
import com.agony.agent.tools.WorkingMemoryTools;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Agony
 * @create: 2026/6/9 9:37
 * @describe:
 */
@Service
public class FullMemoryAgent {

    private final ChatClient chatClient;

    // private final WorkingMemoryTools workingMemoryTools;

    private final RollingMemoryCompressor rollingMemoryCompressor;

    private final MemoryExtractionService memoryExtractionService;

    private final VectorStore vectorStore;

    // 按 userId 隔离的对话历史（实际生产用 Redis，这里简化）
    private final ConcurrentHashMap<String, List<Message>> sessionHistoryMap = new ConcurrentHashMap<>();

    public FullMemoryAgent(DashScopeChatModel dashScopeChatModel,
                           WorkingMemoryTools workingMemoryTools,
                           RollingMemoryCompressor rollingMemoryCompressor,
                           MemoryExtractionService memoryExtractionService,
                           VectorStore vectorStore) {

        this.chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultTools(workingMemoryTools)
                .build();
        // this.workingMemoryTools = workingMemoryTools;
        this.rollingMemoryCompressor = rollingMemoryCompressor;
        this.memoryExtractionService = memoryExtractionService;
        this.vectorStore = vectorStore;
    }

    public String chat(String userId, String message, int maxIterations) {

        // 1. 检索语义记忆（长期记忆），注入系统提示
        String longTermContext = recallLongTerm(userId, message);

        // 2. 加入当前消息（按 userId 隔离）
        List<Message> sessionHistory = sessionHistoryMap.computeIfAbsent(userId, k -> new ArrayList<>());
        sessionHistory.add(new UserMessage(message));

        // 3. 超长时压缩历史（不截断，保留摘要）
        List<Message> messages = rollingMemoryCompressor.maybeCompress(sessionHistory);

        // 4. 执行 Agent 循环（Agent 会自己用 WorkingMemory 工具管中间状态）
        String finalAnswer = runAgentLoop(messages, longTermContext, maxIterations);

        // 5. 异步提炼本轮对话，写入长期记忆
        CompletableFuture.runAsync(() ->
                memoryExtractionService.extractAndStore(userId,
                        "用户：" + message + "\n助手：" + finalAnswer));

        return finalAnswer;
    }

    private String recallLongTerm(String userId, String query) {

        List<Document> memories = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(3)
                        .similarityThreshold(0.65)
                        .filterExpression("userId == '" + userId + "'")
                        .build());

        if (memories.isEmpty()) return "";
        return "关于这个用户的背景信息：\n" +
                memories.stream()
                        .map(Document::getText)
                        .reduce("", (a, b) -> a + "\n- " + b).strip();
    }

    /**
     * 执行 agent loop
     *
     * @param messages
     * @param longTermContext
     * @param maxIterations
     * @return
     */
    private String runAgentLoop(List<Message> messages, String longTermContext, int maxIterations) {

        return chatClient.prompt()
                .system(buildSystem(longTermContext))
                .messages(messages)
                .call()
                .content();
    }

    /**
     * 构建 system prompt
     *
     * @param longTermContext 背景信息
     * @return
     */
    private String buildSystem(String longTermContext) {
        String base = """
                你是一个智能助理。你有工作记忆工具可以使用：
                - memorize：保存当前任务的中间结果
                - recall：检索之前保存的信息
                - listMemory：查看当前记忆中所有内容
                
                执行多步任务时，主动用记忆工具管理中间状态。
                """;
        return longTermContext.isBlank() ? base :
                base + "\n\n用户背景（来自长期记忆）：\n" + longTermContext;
    }
}