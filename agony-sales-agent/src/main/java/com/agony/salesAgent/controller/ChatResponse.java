package com.agony.salesAgent.controller;

public record ChatResponse(
        String sessionId,
        String reply,
        long durationMs
) {
}