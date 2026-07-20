package com.agony.salesAgent.memory;

import com.agony.salesAgent.entity.ChatMemoryEntity;
import com.agony.salesAgent.repository.ChatMemoryRepository;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author: Agony
 * @create: 2026/7/20 13:51
 * @describe:
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class PostgresChatMemoryStore implements ChatMemoryStore {

    private final ChatMemoryRepository repository;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String sessionId = memoryId.toString();
        return repository.findBySessionId(sessionId)
                .map(entity -> {
                    try {
                        return ChatMessageDeserializer.messagesFromJson(entity.getMessages());
                    } catch (Exception e) {
                        log.warn("反序列化对话记忆失败，sessionId={}", sessionId, e);
                        return Collections.<ChatMessage>emptyList();
                    }
                })
                .orElse(Collections.emptyList());
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String sessionId = memoryId.toString();
        try {
            String json = ChatMessageSerializer.messagesToJson(messages);
            ChatMemoryEntity entity = repository.findBySessionId(sessionId)
                    .orElseGet(() -> {
                        ChatMemoryEntity e = new ChatMemoryEntity();
                        e.setSessionId(sessionId);
                        return e;
                    });
            entity.setMessages(json);
            repository.save(entity);
        } catch (Exception e) {
            log.error("保存对话记忆失败，sessionId={}", sessionId, e);
        }
    }

    @Override
    public void deleteMessages(Object memoryId) {
        repository.deleteBySessionId(memoryId.toString());
    }
}