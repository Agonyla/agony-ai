package com.agony.prompt.controller;

import com.agony.prompt.service.SelfConsistencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/21 16:14
 * @describe:
 */

@RestController
@RequestMapping("/self-consistency")
public class SelfConsistencyController {

    private final SelfConsistencyService selfConsistencyService;

    public SelfConsistencyController(SelfConsistencyService selfConsistencyService) {
        this.selfConsistencyService = selfConsistencyService;
    }

    @GetMapping
    public String query(@RequestParam String question, @RequestParam(defaultValue = "5") int sampleCount) throws Exception {

        return selfConsistencyService.query(question, sampleCount);
    }
}