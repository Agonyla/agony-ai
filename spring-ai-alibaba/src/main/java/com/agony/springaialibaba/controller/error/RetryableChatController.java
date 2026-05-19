package com.agony.springaialibaba.controller.error;

import com.agony.springaialibaba.service.RetryableChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/19 16:09
 * @describe:
 */
@RestController
@RequestMapping("/api/retry")
public class RetryableChatController {

    private final RetryableChatService retryableChatService;

    public RetryableChatController(RetryableChatService retryableChatService) {
        this.retryableChatService = retryableChatService;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return retryableChatService.chatWithRetry(message);
    }
}