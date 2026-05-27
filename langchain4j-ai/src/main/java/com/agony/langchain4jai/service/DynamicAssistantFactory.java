package com.agony.langchain4jai.service;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Service;

/**
 * @author: Agony
 * @create: 2026/5/27 15:47
 * @describe:
 */
@Service
public class DynamicAssistantFactory {

    private final ChatModel chatModel;

    public DynamicAssistantFactory(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public TenantChatAssistant createForTenant(String systemPrompt) {

        return AiServices.builder(TenantChatAssistant.class)
                .chatModel(chatModel)
                // 接口用了 @MemoryId，必须用 chatMemoryProvider（每个 sessionId 独立记忆）
                // 用 chatMemory() 会导致 ChatMemoryService.getOrCreateChatMemory NPE
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .systemMessageProvider(memoryId -> systemPrompt)
                .build();
    }
}