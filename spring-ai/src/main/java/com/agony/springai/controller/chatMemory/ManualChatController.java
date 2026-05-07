package com.agony.springai.controller.chatMemory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Agony
 * @create: 2026/5/7 9:25
 * @describe:
 */
@RestController
@RequestMapping("/manual-chat")
public class ManualChatController {

    private final ChatClient chatClient;

    public ManualChatController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    private final Map<String, List<Message>> sessions = new ConcurrentHashMap();

    record ChatRequest(String conversationId, String message) {
    }

    @PostMapping("/chat")
    public String chat(@RequestBody ChatRequest chatRequest) {

        // 获取或创建该会话的历史
        List<Message> history = sessions.computeIfAbsent(chatRequest.conversationId(), id -> {
            List<Message> list = new ArrayList<>();
            list.add(new SystemMessage("你是一个 Java 技术助手"));
            return list;
        });

        // 追加用户消息
        history.add(new UserMessage(chatRequest.message()));

        // 带完整历史调用模型
        String reply = chatClient.prompt()
                .messages(history)
                .call()
                .content();

        // 把模型回复也追加进历史
        history.add(new AssistantMessage(reply));

        return reply;
    }
}