package com.agony.prompt.controller;

import com.agony.prompt.entity.ContractRick;
import com.agony.prompt.service.ContractAnalysisService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/22 10:33
 * @describe:
 */
@RestController
@RequestMapping("/contract")
public class ContractAnalysisController {

    private final ContractAnalysisService contractAnalysisService;

    public ContractAnalysisController(ContractAnalysisService contractAnalysisService) {
        this.contractAnalysisService = contractAnalysisService;
    }

    @PostMapping("/analyze")
    public ContractRick analyze(@RequestBody String clause) throws Exception {

        return contractAnalysisService.analyzeWithConsistency(clause);
    }
}