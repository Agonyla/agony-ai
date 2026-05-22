package com.agony.prompt.entity;

import com.agony.prompt.enums.Verdict;

public record ContractRick(
        Verdict hasRisk,
        String riskType,
        int severity
) {
}