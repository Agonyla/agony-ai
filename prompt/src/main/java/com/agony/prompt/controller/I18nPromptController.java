package com.agony.prompt.controller;

import com.agony.prompt.service.I18nPromptService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

/**
 * @author: Agony
 * @create: 2026/5/26 11:20
 * @describe:
 */
@RestController
@RequestMapping("/i18n-prompt")
public class I18nPromptController {

    private final I18nPromptService i18nPromptService;

    public I18nPromptController(I18nPromptService i18nPromptService) {
        this.i18nPromptService = i18nPromptService;
    }

    @GetMapping("/load")
    public String loadPrompt(@RequestParam String name,
                             @RequestParam(defaultValue = "zh") String lang) {
        return i18nPromptService.loadPrompt(name, Locale.forLanguageTag(lang));
    }
}