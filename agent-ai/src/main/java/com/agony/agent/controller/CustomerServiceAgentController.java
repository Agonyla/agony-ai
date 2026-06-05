package com.agony.agent.controller;

import com.agony.agent.service.CustomerServiceAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/6/5 8:53
 * @describe:
 */
@RestController
@RequestMapping("/api/agent/cs")
public class CustomerServiceAgentController {

    private final CustomerServiceAgent agent;

    public CustomerServiceAgentController(CustomerServiceAgent agent) {
        this.agent = agent;
    }

    record CsRequest(String message) {
    }

    @PostMapping
    public String chat(@RequestBody CsRequest request) {
        return agent.chat(request.message());
    }
}