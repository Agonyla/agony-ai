package com.agony.langchain4jai.config;

import com.agony.langchain4jai.memory.JpaChatMemoryStore;
import com.agony.langchain4jai.service.chatMemory.PersistentChatAssistant;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Agony
 * @create: 2026/5/29 14:08
 * @describe:
 */
@Configuration
public class PersistentMemoryConfig {

    @Bean("chatAssistant")
    public PersistentChatAssistant chatAssistant(ChatModel chatModel, JpaChatMemoryStore jpaChatMemoryStore) {

        return AiServices.builder(PersistentChatAssistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .chatMemoryStore(jpaChatMemoryStore)
                        .build())
                .build();
    }
}