package com.agony.langchain4jai.memory;

import com.agony.langchain4jai.model.ChatMessageEntity;
import com.agony.langchain4jai.repository.ChatMessageRepository;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author: Agony
 * @create: 2026/5/29 13:23
 * @describe:
 */
@Component
public class JpaChatMemoryStore implements ChatMemoryStore {

    private final ChatMessageRepository messageRepo;

    public JpaChatMemoryStore(ChatMessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {

        return messageRepo.findBySessionIdOrderByCreatedAtAsc(memoryId.toString())
                .stream()
                .map(this::toMessage)
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional
    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {

        // 全量替换：先删后插
        // 生产环境可以优化为增量更新（只追加新消息），减少写放大
        messageRepo.deleteBySessionId(memoryId.toString());

        List<ChatMessageEntity> list = messages.stream()
                .map(chatMessage -> toEntity(memoryId.toString(), chatMessage))
                .toList();

        messageRepo.saveAll(list);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        messageRepo.deleteBySessionId(memoryId.toString());
    }

    private ChatMessage toMessage(ChatMessageEntity entity) {

        return switch (entity.getRole()) {
            case "SYSTEM" -> new SystemMessage(entity.getContent());
            case "USER" -> new UserMessage(entity.getContent());
            case "AI" -> new AiMessage(entity.getContent());
            case "TOOL" -> new ToolExecutionResultMessage(
                    entity.getToolName(), entity.getToolName(), entity.getContent());
            default -> null;
        };
    }

    private ChatMessageEntity toEntity(String sessionId, ChatMessage message) {

        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSessionId(sessionId);

        if (message instanceof SystemMessage m) {
            entity.setRole("SYSTEM");
            entity.setContent(m.text());
        } else if (message instanceof UserMessage m) {
            entity.setRole("USER");
            entity.setContent(m.singleText());
        } else if (message instanceof AiMessage m) {
            entity.setRole("AI");
            entity.setContent(m.text() != null ? m.text() : "");
        } else if (message instanceof ToolExecutionResultMessage m) {
            entity.setRole("TOOL");
            entity.setContent(m.text());
            entity.setToolName(m.toolName());
        }

        return entity;
    }
}