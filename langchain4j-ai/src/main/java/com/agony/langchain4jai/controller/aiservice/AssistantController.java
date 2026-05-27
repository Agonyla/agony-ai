package com.agony.langchain4jai.controller.aiservice;

import com.agony.langchain4jai.service.SimpleAssistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/27 14:19
 * @describe:
 */
@RestController
@RequestMapping("/assistant")
public class AssistantController {

    private final SimpleAssistant assistant;

    public AssistantController(SimpleAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping
    public String ask(@RequestParam String question) {
        return assistant.chat(question);
    }
}