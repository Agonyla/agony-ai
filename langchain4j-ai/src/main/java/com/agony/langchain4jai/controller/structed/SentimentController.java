package com.agony.langchain4jai.controller.structed;

import com.agony.langchain4jai.model.SentimentResult;
import com.agony.langchain4jai.service.SentimentAnalyzer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/28 9:47
 * @describe:
 */
@RestController
@RequestMapping("/structured/sentiment")
public class SentimentController {

    private final SentimentAnalyzer analyzer;

    public SentimentController(SentimentAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    @GetMapping
    public SentimentResult analyze(@RequestParam String review) {

        return analyzer.analyze(review);
    }
}