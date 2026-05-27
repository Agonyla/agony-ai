package com.agony.langchain4jai.controller.prompt;

import com.agony.langchain4jai.service.TenantAwareAssistantService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/27 16:40
 * @describe:
 */
@RestController
@RequestMapping("/tenant-chat")
public class TenantChatController {

    private final TenantAwareAssistantService assistantService;

    public TenantChatController(TenantAwareAssistantService assistantService) {
        this.assistantService = assistantService;
    }

    record chatRequest(String tenantId, String sessionId, String message) {
    }

    @PostMapping
    public String chat(@RequestBody chatRequest chatRequest) {
        return assistantService.chat(chatRequest.tenantId(), chatRequest.sessionId(), chatRequest.message());
    }
}