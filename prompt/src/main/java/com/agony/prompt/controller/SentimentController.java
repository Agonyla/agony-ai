package com.agony.prompt.controller;

import com.agony.prompt.service.SentimentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/21 14:42
 * @describe:
 */
@RestController
@RequestMapping("/api/sentiment")
public class SentimentController {

    private final SentimentService sentimentService;

    public SentimentController(SentimentService sentimentService) {

        this.sentimentService = sentimentService;
    }

    @GetMapping("/analyze")
    public String analyze(@RequestParam("comment") String comment) {
        return sentimentService.analyze(comment);
    }

    @PostMapping("/analyzeBatch")
    public Map<String, String> analyzeBatch(@RequestBody List<String> comments) {
        return sentimentService.analyzeBatch(comments);
    }
}