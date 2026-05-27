package com.agony.langchain4jai.controller.prompt;

import com.agony.langchain4jai.service.CodeAssistant;
import com.agony.langchain4jai.service.Translator;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Agony
 * @create: 2026/5/27 15:35
 * @describe:
 */
@RestController
@RequestMapping("/prompt/usr-msg")
public class UserMessageController {

    private final CodeAssistant codeAssistant;
    private final Translator translator;

    public UserMessageController(CodeAssistant codeAssistant, Translator translator) {
        this.codeAssistant = codeAssistant;
        this.translator = translator;
    }

    @GetMapping("/translate")
    public String translate(@RequestParam String language, @RequestParam String text) {
        return translator.translate(language, text);
    }

    @PostMapping("/review")
    public String reviewCode(@RequestBody String code) {
        return codeAssistant.reviewCode(code);
    }
}