package com.agony.langchain4jai.model;

public record ContractParty(
        String role,
        String name,
        String contactPerson
) {
}