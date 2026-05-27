package com.agony.langchain4jai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface TenantAssistant {

    @SystemMessage("你是{{companyName}}的客服助手，服务范围：{{serviceScope}}")
    String chat(@V("companyName") String companyName,
                @V("serviceScope") String serviceScope,
                @UserMessage String message);

}