package com.agony.langchain4jai.controller.dynamic;

import com.agony.langchain4jai.service.dynamic.SceneAwareAgentService;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Agony
 * @create: 2026/5/28 17:59
 * @describe:
 */
@RestController
@RequestMapping("/dynamic/scene")
public class SceneAgentController {

    private final SceneAwareAgentService agentService;

    public SceneAgentController(SceneAwareAgentService agentService) {
        this.agentService = agentService;
    }

    @GetMapping
    public String chat(@RequestHeader(value = "X-Session-Id", defaultValue = "default") String sessionId,
                       @RequestParam(defaultValue = "pre_sale") String scene,
                       @RequestParam String message) {
        return agentService.chat(sessionId, scene, message);
    }
}