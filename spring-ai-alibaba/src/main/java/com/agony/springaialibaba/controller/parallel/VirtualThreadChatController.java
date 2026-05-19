package com.agony.springaialibaba.controller.parallel;

import com.agony.springaialibaba.service.VirtualThreadChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/19 9:26
 * @describe:
 */
@RestController
@RequestMapping("/api/virtual")
public class VirtualThreadChatController {

    private final VirtualThreadChatService virtualThreadChatService;

    public VirtualThreadChatController(VirtualThreadChatService virtualThreadChatService) {
        this.virtualThreadChatService = virtualThreadChatService;
    }

    @GetMapping("/fastest")
    public String fastest(@RequestParam String question) throws Exception {
        return virtualThreadChatService.fastestResponse(question);
    }

    @GetMapping("/all")
    public Map<String, String> all(@RequestParam String question) throws Exception {
        return virtualThreadChatService.allResponse(question);
    }
}