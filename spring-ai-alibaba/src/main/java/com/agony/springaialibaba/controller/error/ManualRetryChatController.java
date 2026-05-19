package com.agony.springaialibaba.controller.error;

import com.agony.springaialibaba.service.ManualRetryChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/19 17:45
 * @describe:
 */
@RestController
@RequestMapping("/api/manual-retry")
public class ManualRetryChatController {

    private final ManualRetryChatService manualRetryChatService;

    public ManualRetryChatController(ManualRetryChatService manualRetryChatService) {
        this.manualRetryChatService = manualRetryChatService;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return manualRetryChatService.chatWithManualRetry(message);
    }
}