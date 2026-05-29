package com.agony.langchain4jai.controller.chatMemory;

import com.agony.langchain4jai.service.chatMemory.SessionManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/29 15:26
 * @describe:
 */
@RestController
@RequestMapping("/sessions")
public class SessionManagementController {

    private final SessionManagementService sessionService;

    public SessionManagementController(SessionManagementService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<SessionManagementService.SessionSummary> getSessions(
            @RequestParam String userId) {
        return sessionService.getUserSessions(userId);
    }

    @PostMapping("/new")
    public Map<String, String> newSession(@RequestParam String userId) {
        return Map.of("sessionId", sessionService.newSession(userId));
    }

    @DeleteMapping("/{sessionId}")
    public void deleteSession(@PathVariable String sessionId,
                              @RequestParam String userId) {
        sessionService.deleteSession(sessionId, userId);
    }
}