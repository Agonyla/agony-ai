package com.agony.langchain4jai.controller.dynamic;

import com.agony.langchain4jai.model.UserRole;
import com.agony.langchain4jai.service.dynamic.CachedDynamicAgentService;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Agony
 * @create: 2026/5/28 16:27
 * @describe:
 */
@RestController
@RequestMapping("/dynamic/agent/cached")
public class CachedDynamicAgentController {

    private final CachedDynamicAgentService agentService;

    public CachedDynamicAgentController(CachedDynamicAgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping
    public String chat(@RequestHeader(value = "X-Session-Id", defaultValue = "default") String sessionId,
                       @RequestParam(defaultValue = "GUEST") UserRole role,
                       @RequestParam String message) {
        return agentService.chat(sessionId, role, message);
    }
}