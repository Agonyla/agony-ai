package com.agony.springaialibaba.controller.error;

import com.agony.springaialibaba.service.SafeChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/19 15:55
 * @describe:
 */
@RestController
@RequestMapping("/api/safe")
public class SafeChatController {

    private final SafeChatService safeChatService;

    public SafeChatController(SafeChatService safeChatService) {
        this.safeChatService = safeChatService;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return safeChatService.safeChat(message);
    }
}