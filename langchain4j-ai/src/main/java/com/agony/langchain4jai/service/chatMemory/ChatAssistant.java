package com.agony.langchain4jai.service.chatMemory;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

/**
 * @author: Agony
 * @create: 2026/5/29 12:55
 * @describe:
 */
public interface ChatAssistant {

    @SystemMessage("你是一个 Java 技术助手，记住用户在对话中提到的技术栈和问题背景")
    String chat(@MemoryId String sessionId, @UserMessage String message);
}