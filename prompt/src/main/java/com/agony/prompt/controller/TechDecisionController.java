package com.agony.prompt.controller;

import com.agony.prompt.service.TechDecisionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/21 15:37
 * @describe:
 */
@RestController
@RequestMapping("/api/tech-eval")
public class TechDecisionController {

    private final TechDecisionService techDecisionService;

    public TechDecisionController(TechDecisionService techDecisionService) {
        this.techDecisionService = techDecisionService;
    }

    @PostMapping
    public TechDecisionService.TechEvaluation evaluate(@RequestBody EvalRequest request) {
        return techDecisionService.evaluate(request.proposal(), request.context());
    }

    record EvalRequest(String proposal, String context) {
    }
}