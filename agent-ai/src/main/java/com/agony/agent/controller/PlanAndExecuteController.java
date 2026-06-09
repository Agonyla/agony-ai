package com.agony.agent.controller;

import com.agony.agent.service.PlanAndExecuteAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/6/9 18:27
 * @describe:
 */
@RestController
@RequestMapping("/api/agent/plan")
public class PlanAndExecuteController {

    private final PlanAndExecuteAgent agent;

    public PlanAndExecuteController(PlanAndExecuteAgent agent) {
        this.agent = agent;
    }

    @PostMapping
    public PlanAndExecuteAgent.ExecutionResult run(@RequestBody TaskRequest request) {
        return agent.run(request.task());
    }

    record TaskRequest(String task) {
    }
}