package com.agony.langchain4jai.config;

import com.agony.langchain4jai.service.tools.OrderAssistant;
import com.agony.langchain4jai.tools.OrderQueryTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author: Agony
 * @create: 2026/5/28 11:23
 * @describe:
 */
@Configuration
public class OrderAssistantConfig {

    @Bean
    @Primary
    public OrderAssistant orderAssistantWithTools(ChatModel model, OrderQueryTools orderQueryTools) {
        return AiServices.builder(OrderAssistant.class)
                .chatModel(model)
                .tools(orderQueryTools)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
    }
}