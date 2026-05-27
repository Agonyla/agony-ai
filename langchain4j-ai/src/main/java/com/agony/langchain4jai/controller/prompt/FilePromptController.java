package com.agony.langchain4jai.controller.prompt;

import com.agony.langchain4jai.service.FileBasedAssistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/27 15:45
 * @describe:
 */
@RestController
@RequestMapping("/prompt/file")
public class FilePromptController {

    private final FileBasedAssistant assistant;

    public FilePromptController(FileBasedAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping
    public String chat(@RequestParam String company,
                       @RequestParam String scope,
                       @RequestParam String message) {
        return assistant.chat(company, scope, message);
    }
}