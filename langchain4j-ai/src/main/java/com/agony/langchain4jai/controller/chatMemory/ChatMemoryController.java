package com.agony.langchain4jai.controller.chatMemory;

import com.agony.langchain4jai.service.chatMemory.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * @author: Agony
 * @create: 2026/5/29 12:59
 * @describe:
 */
@RestController
@RequestMapping("/memoroy/chat")
public class ChatMemoryController {

    private final ChatService chatService;

    public ChatMemoryController(ChatService chatService) {
        this.chatService = chatService;
    }

    record ChatRequest(String message) {
    }

    /**
     * 多轮对话接口
     * X-Session-Id Header 用来标识会话，相同值共享对话历史
     *
     * @param chatRequest
     * @param sessionId
     * @return
     */
    @PostMapping
    public Map<String, String> chat(
            @RequestBody ChatRequest chatRequest,
            @RequestHeader(value = "X-Session-Id", defaultValue = "default") String sessionId
    ) {
        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }
        String reply = chatService.chat(sessionId, chatRequest.message());

        return Map.of(
                "sessionId", sessionId,
                "message", reply
        );
    }

    /**
     * 开始新会话：生成新 sessionId，等同于清空历史
     *
     * @return
     */
    @PostMapping("/new-session")
    public Map<String, String> newSession() {
        String newSessionId = UUID.randomUUID().toString();
        return Map.of("sessionId", newSessionId);
    }
}