package com.agony.langchain4jai.controller.chat;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/27 9:16
 * @describe:
 */
@RestController
@RequestMapping("/chat")
public class SimpleChatController {

    private final ChatModel chatModel;

    public SimpleChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return chatModel.chat(message);
    }
}