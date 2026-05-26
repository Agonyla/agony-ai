package com.agony.prompt.controller;

import com.agony.prompt.entity.TenantConfig;
import com.agony.prompt.service.TenantPromptService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/26 10:22
 * @describe:
 */
@RestController
@RequestMapping("/tenant-prompt")
public class TenantPromptController {

    private final TenantPromptService tenantPromptService;

    public TenantPromptController(TenantPromptService tenantPromptService) {
        this.tenantPromptService = tenantPromptService;
    }

    @PostMapping("/generate")
    public String generateTenantPrompt(@RequestBody TenantConfig config) {
        return tenantPromptService.generateTenantPrompt(config);
    }
}