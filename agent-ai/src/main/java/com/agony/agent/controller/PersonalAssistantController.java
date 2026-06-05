package com.agony.agent.controller;

import com.agony.agent.service.PersonalAssistantAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/6/5 9:36
 * @describe:
 */
@RestController
@RequestMapping("/api/agent/assistant")
public class PersonalAssistantController {

    private final PersonalAssistantAgent agent;

    record ChatRequest(String message) {
    }

    public PersonalAssistantController(PersonalAssistantAgent agent) {
        this.agent = agent;
    }

    @PostMapping
    public String chat(@RequestBody ChatRequest chatRequest) {
        return agent.chat(chatRequest.message);
    }
}