package com.agony.langchain4jai.service;

import com.agony.langchain4jai.model.TenantPrompt;
import com.agony.langchain4jai.repository.TenantPromptRepository;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Agony
 * @create: 2026/5/27 16:21
 * @describe:
 */
@Service
public class TenantAwareAssistantService {

    private final ChatModel chatModel;
    private final TenantPromptRepository promptRepo;
    private final Map<String, TenantChatAssistant> assistantCache = new ConcurrentHashMap<>();

    public TenantAwareAssistantService(ChatModel chatModel, TenantPromptRepository promptRepo) {
        this.chatModel = chatModel;
        this.promptRepo = promptRepo;
    }

    public String chat(String tenantId, String sessionId, String message) {

        TenantChatAssistant assistant = assistantCache.computeIfAbsent(tenantId, id -> {

            String systemPrompt = promptRepo.findByTenantId(id)
                    .map(TenantPrompt::getContent)
                    .orElse("你是一个通用助手");

            return AiServices.builder(TenantChatAssistant.class)
                    .chatModel(chatModel)
                    .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                    .systemMessageProvider(memoryId -> systemPrompt)
                    .build();
        });

        return assistant.chat(sessionId, message);
    }
}