package com.agony.springai.controller.prompt;

import com.agony.springai.service.CodeReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/3/26 16:29
 * @describe:
 */
@RestController
@RequestMapping("code-with-temp")
public class CodeReviewWithTemplateController {

    private final CodeReviewService codeReviewService;

    public CodeReviewWithTemplateController(CodeReviewService codeReviewService) {
        this.codeReviewService = codeReviewService;
    }

    @GetMapping("/review")
    public String review(@RequestParam(defaultValue = "java") String language,
                         @RequestParam String code) {
        return codeReviewService.review(language, code);
    }
}