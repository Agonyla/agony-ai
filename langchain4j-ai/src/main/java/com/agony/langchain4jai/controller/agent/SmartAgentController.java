package com.agony.langchain4jai.controller.agent;

import com.agony.langchain4jai.service.agent.SmartAgent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/6/2 10:17
 * @describe:
 */
@RestController
@RequestMapping("/agent/loop")
public class SmartAgentController {

    private final SmartAgent agent;

    public SmartAgentController(SmartAgent agent) {
        this.agent = agent;
    }

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody AgentRequest req) {
        String reply = agent.chat(req.sessionId(), req.message());
        return Map.of("sessionId", req.sessionId(), "reply", reply);
    }

    record AgentRequest(String sessionId, String message) {
    }
}