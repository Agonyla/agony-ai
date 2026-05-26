package com.agony.prompt.repository;

import com.agony.prompt.entity.PromptTemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: Agony
 * @create: 2026/5/26 18:11
 * @describe:
 */
@Repository
public interface PromptTemplateRepository extends JpaRepository<PromptTemplateEntity, Long> {

    // 查当前环境的激活版本
    Optional<PromptTemplateEntity> findByTemplateKeyAndStatusAndEnvironment(
            String key, String status, String environment);

    // 查某个 key 的所有历史版本
    List<PromptTemplateEntity> findByTemplateKeyAndEnvironmentOrderByCreatedAtDesc(
            String key, String environment);

    // 查某个具体版本
    Optional<PromptTemplateEntity> findByTemplateKeyAndVersionAndEnvironment(
            String key, String version, String environment);
}