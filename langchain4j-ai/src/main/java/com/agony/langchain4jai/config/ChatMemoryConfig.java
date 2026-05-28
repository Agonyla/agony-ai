package com.agony.langchain4jai.config;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Agony
 * @create: 2026/5/28 10:47
 * @describe:
 */
@Configuration
public class ChatMemoryConfig {

    // 提供全局 ChatMemoryProvider，所有 @AiService 使用 @MemoryId 时都会用到
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.withMaxMessages(10);
    }
}