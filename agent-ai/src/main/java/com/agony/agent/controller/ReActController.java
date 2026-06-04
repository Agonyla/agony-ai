package com.agony.agent.controller;

import com.agony.agent.service.SimpleReActAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/6/4 16:09
 * @describe:
 */
@RestController
@RequestMapping("/api/agent/react")
public class ReActController {

    private final SimpleReActAgent simpleReActAgent;

    public ReActController(SimpleReActAgent simpleReActAgent) {
        this.simpleReActAgent = simpleReActAgent;
    }

    record TaskRequest(String task) {
    }

    @PostMapping
    public String run(@RequestBody TaskRequest request) {
        return simpleReActAgent.run(request.task(), 10);
    }
}