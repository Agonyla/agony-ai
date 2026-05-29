package com.agony.langchain4jai.controller.chatMemory;

import com.agony.langchain4jai.service.chatMemory.PersistentChatAssistant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * @author: Agony
 * @create: 2026/5/29 14:11
 * @describe:
 */
@RestController
@RequestMapping("/persistant/chat")
public class PersistentChatController {

    private final PersistentChatAssistant assistant;

    public PersistentChatController(@Qualifier("chatAssistant") PersistentChatAssistant assistant) {
        this.assistant = assistant;
    }

    record ChatRequest(String message) {

    }

    @PostMapping
    public Map<String, String> chat(
            @RequestBody ChatRequest request,
            @RequestHeader(value = "X-Session-Id", defaultValue = "default") String sessionId) {

        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString();
        }

        String reply = assistant.chat(sessionId, request.message());

        return Map.of(
                "sessionId", sessionId,
                "reply", reply
        );
    }
}