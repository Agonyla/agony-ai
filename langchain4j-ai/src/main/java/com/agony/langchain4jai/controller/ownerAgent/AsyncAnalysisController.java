package com.agony.langchain4jai.controller.ownerAgent;

import com.agony.langchain4jai.service.ownerAgent.AsyncAnalysisService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/6/3 9:27
 * @describe:
 */
@RestController
@RequestMapping("/analysis/async")
public class AsyncAnalysisController {

    private final AsyncAnalysisService asyncAnalysisService;

    public AsyncAnalysisController(AsyncAnalysisService asyncAnalysisService) {
        this.asyncAnalysisService = asyncAnalysisService;
    }

    record SubmitRequest(String question) {

    }

    @PostMapping("/submit")
    public Map<String, String> submit(@RequestBody SubmitRequest request) {
        String taskId = asyncAnalysisService.submitTask(request.question);
        return Map.of("taskId", taskId, "message", "任务已提交，请用 taskId 轮询状态");
    }

    @GetMapping("/status/{taskId}")
    public AsyncAnalysisService.TaskStatus getStatus(@PathVariable String taskId) {
        return asyncAnalysisService.getStatus(taskId);
    }

}