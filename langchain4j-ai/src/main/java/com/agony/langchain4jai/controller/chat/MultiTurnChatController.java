package com.agony.langchain4jai.controller.chat;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/27 12:51
 * @describe: 用 Messages 做多轮对话
 */
@RestController
@RequestMapping("/chat")
public class MultiTurnChatController {

    private final ChatModel chatModel;

    public MultiTurnChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    record HistoryMessage(String user, String assistant) {
    }

    record MultiTurnRequest(
            List<HistoryMessage> history,
            String message
    ) {
    }

    @PostMapping("/multi-chat")
    public String multiTurnChat(@RequestBody MultiTurnRequest request) {

        List<ChatMessage> messages = new ArrayList<>();

        // 系统消息（角色设定）
        messages.add(new SystemMessage("你是一个Java助手，负责回复Java相关的问题"));

        // 历史对话
        for (HistoryMessage h : request.history()) {

            messages.add(new UserMessage(h.user()));
            messages.add(new AiMessage(h.assistant()));
        }

        // 当前用户消息
        messages.add(new UserMessage(request.message()));

        AiMessage aiMessage = chatModel.chat(messages).aiMessage();

        return aiMessage.text();
    }
}