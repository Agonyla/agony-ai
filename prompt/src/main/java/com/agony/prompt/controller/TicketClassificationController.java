package com.agony.prompt.controller;

import com.agony.prompt.service.TicketClassificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/21 15:09
 * @describe:
 */
@RestController
@RequestMapping("/api/ticket")
public class TicketClassificationController {

    private final TicketClassificationService classificationService;

    public TicketClassificationController(TicketClassificationService classificationService) {
        this.classificationService = classificationService;
    }

    @GetMapping("/classify")
    public String classify(@RequestParam String ticket) {
        return classificationService.classify(ticket);
    }
}