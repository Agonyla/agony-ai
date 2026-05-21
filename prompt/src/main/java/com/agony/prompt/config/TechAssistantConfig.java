package com.agony.prompt.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Agony
 * @create: 2026/5/21 14:11
 * @describe:
 */
@Configuration
public class TechAssistantConfig {

    @Bean("techAssistantClient")
    public ChatClient techAssistantClient(DashScopeChatModel dashScopeChatModel) {

        return ChatClient.builder(dashScopeChatModel)
                .defaultSystem("""
                        你是一个 Java 技术助手。
                        只回答 Java 技术相关问题，不确定的内容说不知道，代码用 Java 17 语法。""")
                .build();
    }

}