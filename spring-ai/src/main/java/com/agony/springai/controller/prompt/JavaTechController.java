package com.agony.springai.controller.prompt;

import com.agony.springai.service.JavaTechService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/3/24 13:34
 * @describe:
 */
@RestController
@RequestMapping("java-tech")
public class JavaTechController {

    private final JavaTechService javaTechService;

    public JavaTechController(JavaTechService javaTechService) {
        this.javaTechService = javaTechService;
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        return javaTechService.ask(question);
    }
}