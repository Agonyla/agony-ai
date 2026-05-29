package com.agony.langchain4jai.repository;

import com.agony.langchain4jai.model.TenantPrompt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantPromptRepository extends JpaRepository<TenantPrompt, Long> {
    Optional<TenantPrompt> findByTenantId(String tenantId);
}