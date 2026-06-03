package com.agony.langchain4jai.service.ownerAgent;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Agony
 * @create: 2026/6/3 9:25
 * @describe:
 */
@Service
public class AsyncAnalysisService {

    private final AnalysisAgent analysisAgent;

    private final Map<String, TaskStatus> taskMap = new ConcurrentHashMap<>();

    public AsyncAnalysisService(AnalysisAgent analysisAgent) {
        this.analysisAgent = analysisAgent;
    }

    public record TaskStatus(String status, String result, String error) {
    }

    public TaskStatus getStatus(String taskId) {

        return taskMap.getOrDefault(taskId, new TaskStatus("NOT_FOUND", null, null));
    }

    public String submitTask(String question) {

        String taskId = UUID.randomUUID().toString();
        taskMap.put(taskId, new TaskStatus("RUNNING", null, null));

        CompletableFuture.supplyAsync(() -> analysisAgent.analyze(taskId, question))
                .whenComplete((result, error) -> {
                    if (error != null) {
                        taskMap.put(taskId, new TaskStatus("FAILED", null, error.getMessage()));
                    } else {
                        taskMap.put(taskId, new TaskStatus("SUCCESS", result, null));
                    }
                });

        return taskId;
    }
}