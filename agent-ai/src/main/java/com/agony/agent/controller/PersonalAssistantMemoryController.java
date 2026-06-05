package com.agony.agent.controller;

import com.agony.agent.service.PersonalAssistantAgentWithMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/6/5 9:59
 * @describe:
 */
@RestController
@RequestMapping("/api/agent/assistant/memory")
public class PersonalAssistantMemoryController {

    private final PersonalAssistantAgentWithMemory agent;

    public PersonalAssistantMemoryController(PersonalAssistantAgentWithMemory agent) {
        this.agent = agent;
    }

    @PostMapping
    public String chat(@RequestBody ChatMemoryRequest request) {
        return agent.chat(request.message(), request.sessionId());
    }

    record ChatMemoryRequest(String message, String sessionId) {
    }
}