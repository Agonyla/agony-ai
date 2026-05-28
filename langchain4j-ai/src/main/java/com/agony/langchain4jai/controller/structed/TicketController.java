package com.agony.langchain4jai.controller.structed;

import com.agony.langchain4jai.model.TicketCategory;
import com.agony.langchain4jai.service.TicketClassifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/28 10:36
 * @describe:
 */
@RestController
@RequestMapping("/structured/ticket")
public class TicketController {

    private final TicketClassifier classifier;

    public TicketController(TicketClassifier classifier) {
        this.classifier = classifier;
    }

    @GetMapping
    public TicketCategory classify(@RequestParam String ticket) {
        return classifier.classify(ticket);
    }
}