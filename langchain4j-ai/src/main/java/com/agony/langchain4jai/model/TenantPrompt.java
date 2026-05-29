package com.agony.langchain4jai.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * @author: Agony
 * @create: 2026/5/27 16:12
 * @describe:
 */
@Entity
@Table(name = "tenant_prompt")
@Data
public class TenantPrompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String tenantId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;
}