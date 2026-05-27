package com.agony.langchain4jai.controller.prompt;

import com.agony.langchain4jai.service.TenantAssistant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/27 15:01
 * @describe:
 */
@RestController
@RequestMapping("/prompt/tenant")
public class TenantAssistantController {

    private final TenantAssistant tenantAssistant;

    public TenantAssistantController(TenantAssistant tenantAssistant) {
        this.tenantAssistant = tenantAssistant;
    }

    @GetMapping
    public String chat(@RequestParam String company,
                       @RequestParam String scope,
                       @RequestParam String message) {
        return tenantAssistant.chat(company, scope, message);
    }
}