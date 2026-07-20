package com.agony.salesAgent.controller;

import com.agony.salesAgent.agent.SalesAgent;
import com.agony.salesAgent.memory.PostgresChatMemoryStore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * @author: Agony
 * @create: 2026/7/20 13:56
 * @describe:
 */
@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
@Slf4j
public class SalesAgentController {

    private final SalesAgent salesAgent;
    private final PostgresChatMemoryStore chatMemoryStore;

    @GetMapping("/simple-chat")
    public String simpleChat(@RequestParam("sessionId") String sessionId,
                             @RequestParam("message") String message) {

        return salesAgent.chat(sessionId, message, LocalDate.now().toString());
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@Valid @RequestBody ChatRequest chatRequest) {

        log.info("请求接收：sessionId={}, message={}", chatRequest.sessionId(), chatRequest.message());
        long start = System.currentTimeMillis();

        String reply = salesAgent.chat(chatRequest.sessionId(), chatRequest.message(), LocalDate.now().toString());

        long duration = System.currentTimeMillis() - start;

        log.info("请求完成：sessionId={}, durationMs={}", chatRequest.sessionId(), duration);

        return ResponseEntity.ok(new ChatResponse(chatRequest.sessionId(), reply, duration));
    }

    @DeleteMapping("/session/{sessionId}")
    public ResponseEntity<Void> clearSession(@PathVariable String sessionId) {
        chatMemoryStore.deleteMessages(sessionId);
        return ResponseEntity.ok().build();
    }
}