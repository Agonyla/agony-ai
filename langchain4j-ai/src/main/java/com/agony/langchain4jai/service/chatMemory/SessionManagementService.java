package com.agony.langchain4jai.service.chatMemory;

import com.agony.langchain4jai.memory.JpaChatMemoryStore;
import com.agony.langchain4jai.model.ChatMessageEntity;
import com.agony.langchain4jai.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/29 14:56
 * @describe:
 */
@Service
public class SessionManagementService {

    private final ChatMessageRepository chatMessageRepo;
    private final JpaChatMemoryStore jpaChatMemoryStore;

    public SessionManagementService(ChatMessageRepository chatMessageRepo, JpaChatMemoryStore jpaChatMemoryStore) {
        this.chatMessageRepo = chatMessageRepo;
        this.jpaChatMemoryStore = jpaChatMemoryStore;
    }

    public record SessionSummary(
            String sessionId,
            String summary,
            LocalDateTime lastActive
    ) {
    }

    /**
     * 查询用户的所有历史会话（用第一条 USER 消息作为摘要）
     *
     * @param userId
     * @return
     */
    public List<SessionSummary> getUserSessions(String userId) {

        String prefix = userId + "_";

        return chatMessageRepo.findDistinctSessionIdsByPrefix(prefix)
                .stream()
                .map(sessionId -> {
                    List<ChatMessageEntity> messages = chatMessageRepo.findBySessionIdOrderByCreatedAtAsc(sessionId);

                    String summary = messages.stream()
                            .filter(chatMessageEntity -> "USER".startsWith(chatMessageEntity.getRole()))
                            .findFirst()
                            .map(chatMessageEntity -> chatMessageEntity.getContent().substring(0, Math.min(50, chatMessageEntity.getContent().length())))
                            .orElse("新对话");

                    LocalDateTime lastActive = messages.isEmpty()
                            ? LocalDateTime.now()
                            : messages.getLast().getCreatedAt();

                    return new SessionSummary(sessionId, summary, lastActive);
                })
                .toList();
    }

    /**
     * 删除会话（校验归属权，防止越权删除）
     *
     * @param sessionId
     * @param userId
     */
    public void deleteSession(String sessionId, String userId) {
        if (!sessionId.startsWith(userId + "_")) {
            throw new SecurityException("无权删除此会话");
        }
        jpaChatMemoryStore.deleteMessages(sessionId);
    }

    /**
     * 生成新会话 ID
     */
    public String newSession(String userId) {
        return userId + "_" + System.currentTimeMillis();
    }
}