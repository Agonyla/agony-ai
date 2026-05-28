package com.agony.langchain4jai.controller.structed;

import com.agony.langchain4jai.service.AdvancedAssistant;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Agony
 * @create: 2026/5/28 10:47
 * @describe:
 */
@RestController
@RequestMapping("/structured/advanced")
public class AdvancedAssistantController {

    private final AdvancedAssistant assistant;

    public AdvancedAssistantController(AdvancedAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping
    public String process(@RequestParam String role,
                          @RequestParam String company,
                          @RequestParam String taskDesc,
                          @RequestParam String content,
                          @RequestHeader(value = "X-Session-Id", defaultValue = "default") String sessionId) {
        return assistant.process(role, company, taskDesc, content, sessionId);
    }
}