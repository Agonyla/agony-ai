package com.agony.langchain4jai.repository;

import com.agony.langchain4jai.model.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/29 13:21
 * @describe:
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    void deleteBySessionId(String sessionId);

    // 查询以指定前缀开头的所有不重复 sessionId（用于按用户查历史会话）
    @Query("SELECT DISTINCT e.sessionId FROM ChatMessageEntity e WHERE e.sessionId LIKE :prefix%")
    List<String> findDistinctSessionIdsByPrefix(@Param("prefix") String prefix);
}