package com.agony.langchain4jai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface CodeAssistant {

    @SystemMessage("你是一个Java代码审核专家")
    String reviewCode(@UserMessage String code);
    //                ↑ 明确标注这个参数是用户消息
}