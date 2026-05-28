package com.agony.langchain4jai.controller.structed;

import com.agony.langchain4jai.model.ContractInfo;
import com.agony.langchain4jai.service.ContractExtractor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/28 10:43
 * @describe:
 */
@RestController
@RequestMapping("/structured/contract")
public class ContractController {

    private final ContractExtractor extractor;

    public ContractController(ContractExtractor extractor) {
        this.extractor = extractor;
    }

    @PostMapping
    public ContractInfo extract(@RequestBody String contractText) {
        return extractor.extract(contractText);
    }
}