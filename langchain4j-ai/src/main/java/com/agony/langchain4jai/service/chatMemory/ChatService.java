package com.agony.langchain4jai.service.chatMemory;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author: Agony
 * @create: 2026/5/29 12:56
 * @describe:
 */
@Slf4j
@Service
public class ChatService {

    private final ChatAssistant assistant;

    public ChatService(ChatModel chatModel) {

        this.assistant = AiServices.builder(ChatAssistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }

    public String chat(String sessionId, String message) {

        log.info("会话: [{}], 消息: [{}]", sessionId, message);
        String response = assistant.chat(sessionId, message);
        log.info("会话: [{}], 回复: [{}]", sessionId, response);

        return response;
    }
}