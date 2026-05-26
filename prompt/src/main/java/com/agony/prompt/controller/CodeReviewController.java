package com.agony.prompt.controller;

import com.agony.prompt.service.CodeReviewService;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Agony
 * @create: 2026/5/26 11:15
 * @describe:
 */
@RestController
@RequestMapping("/code-review")
public class CodeReviewController {

    private final CodeReviewService codeReviewService;

    public CodeReviewController(CodeReviewService codeReviewService) {
        this.codeReviewService = codeReviewService;
    }

    @PostMapping("/review")
    public String review(@RequestBody String code,
                         @RequestParam(defaultValue = "Java") String language) {
        return codeReviewService.review(code, language);
    }
}