package com.agony.salesAgent.controller;

import com.agony.salesAgent.agent.SalesAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author: Agony
 * @create: 2026/7/20 13:56
 * @describe:
 */
@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
public class SalesAgentController {

    private final SalesAgent salesAgent;

    @GetMapping("/chat")
    public String chat(@RequestParam("sessionId") String sessionId,
                       @RequestParam("message") String message) {

        return salesAgent.chat(sessionId, message, LocalDate.now().toString());
    }
}