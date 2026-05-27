package com.agony.langchain4jai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface SimpleAssistant {

    @SystemMessage("你是一个有好的 AI 助手， 用简洁的语言回答问题")
    String chat(String userMessage);
}