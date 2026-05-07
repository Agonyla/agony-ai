package com.agony.springai.controller.chatMemory;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/7 15:54
 * @describe:
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    private final ChatMemory chatMemory;

    public SessionController(ChatMemory chatMemory) {
        this.chatMemory = chatMemory;
    }

    @DeleteMapping("/{conversationId}")
    public void clearHistory(@PathVariable String conversationId) {
        chatMemory.clear(conversationId);
    }
}