package com.agony.springaialibaba.controller.parallel;

import com.agony.springaialibaba.service.ParallelChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/18 18:32
 * @describe:
 */
@RestController
@RequestMapping("/api/parallel")
public class ParallelChatController {

    private final ParallelChatService parallelChatService;

    public ParallelChatController(ParallelChatService parallelChatService) {
        this.parallelChatService = parallelChatService;
    }

    @GetMapping
    public Map<String, String> chat(@RequestParam String question) throws Exception {

        return parallelChatService.parallelChat(question);
    }
}