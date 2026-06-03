package com.agony.langchain4jai.controller.ownerAgent;

import com.agony.langchain4jai.service.ownerAgent.AnalysisAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/6/3 9:25
 * @describe:
 */
@RestController
@RequestMapping("/analysis")
public class AnalysisController {

    private final AnalysisAgent analysisAgent;

    public AnalysisController(AnalysisAgent analysisAgent) {
        this.analysisAgent = analysisAgent;
    }

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody ChatRequest req) {
        String result = analysisAgent.analyze(req.sessionId(), req.question());
        return Map.of("sessionId", req.sessionId(), "result", result);
    }

    record ChatRequest(String sessionId, String question) {
    }
}