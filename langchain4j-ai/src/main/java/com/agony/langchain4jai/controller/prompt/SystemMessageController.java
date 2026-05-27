package com.agony.langchain4jai.controller.prompt;

import com.agony.langchain4jai.service.Assistant;
import com.agony.langchain4jai.service.CustomerServiceAssistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/27 14:56
 * @describe:
 */
@RestController
@RequestMapping("/prompt")
public class SystemMessageController {

    private final Assistant assistant;
    private final CustomerServiceAssistant csAssistant;

    public SystemMessageController(Assistant assistant,
                                   CustomerServiceAssistant csAssistant) {
        this.assistant = assistant;
        this.csAssistant = csAssistant;
    }

    @GetMapping("/tech")
    public String techChat(@RequestParam String question) {
        return assistant.chat(question);
    }

    @GetMapping("/cs")
    public String csChat(@RequestParam String message) {
        return csAssistant.chat(message);
    }
}