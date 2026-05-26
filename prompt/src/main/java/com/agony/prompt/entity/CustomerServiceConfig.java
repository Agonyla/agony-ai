package com.agony.prompt.entity;

import java.util.List;

public record CustomerServiceConfig(
        String companyName,
        String assistantName,
        List<String> serviceScope,
        List<String> sensitiveTopics,
        String tone
) {
}