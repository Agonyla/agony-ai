package com.agony.agent.controller;

import com.agony.agent.service.FullMemoryAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/6/9 9:54
 * @describe:
 */
@RestController
@RequestMapping("/api/agent/full-memory")
public class FullMemoryAgentController {

    private final FullMemoryAgent agent;

    public FullMemoryAgentController(FullMemoryAgent agent) {
        this.agent = agent;
    }

    @PostMapping
    public String chat(@RequestBody MemoryRequest request) {
        return agent.chat(request.userId(), request.message(), 8);
    }

    record MemoryRequest(String userId, String message) {
    }
}