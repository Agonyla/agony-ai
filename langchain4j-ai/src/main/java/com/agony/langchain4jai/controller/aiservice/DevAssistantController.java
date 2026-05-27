package com.agony.langchain4jai.controller.aiservice;

import com.agony.langchain4jai.service.MultiCapabilityAssistant;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/27 14:23
 * @describe:
 */
@RestController
@RequestMapping("/dev")
public class DevAssistantController {

    private final MultiCapabilityAssistant assistant;

    public DevAssistantController(MultiCapabilityAssistant assistant) {
        this.assistant = assistant;
    }

    @PostMapping("/review")
    public String reviewCode(@RequestBody String code) {
        return assistant.reviewCode(code);
    }

    @PostMapping("/doc")
    public String writeDoc(@RequestBody String techContent) {
        return assistant.writeDoc(techContent);
    }

    @PostMapping("/sql")
    public String optimizeSql(@RequestBody String sql) {
        return assistant.optimizeSql(sql);
    }
}