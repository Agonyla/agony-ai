package com.agony.langchain4jai.controller.prompt;

import com.agony.langchain4jai.service.MultiScenarioAssistant;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Agony
 * @create: 2026/5/27 16:50
 * @describe:
 */
@RestController
@RequestMapping("/prompt/multi")
public class MultiScenarioController {

    private final MultiScenarioAssistant assistant;

    public MultiScenarioController(MultiScenarioAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping("/tech")
    public String techChat(@RequestParam String question) {
        return assistant.techChat(question);
    }

    @GetMapping("/translate")
    public String translate(@RequestParam String language,
                            @RequestParam String content) {
        return assistant.translate(language, content);
    }

    @PostMapping("/review")
    public String reviewCode(@RequestParam String language,
                             @RequestBody String code) {
        return assistant.reviewCode(language, code);
    }

    @GetMapping("/analyze")
    public String analyzeData(@RequestParam String period,
                              @RequestParam String focus,
                              @RequestParam String data) {
        return assistant.analyzeData(period, focus, data);
    }
}