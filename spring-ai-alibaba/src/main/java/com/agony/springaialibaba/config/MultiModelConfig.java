package com.agony.springaialibaba.config;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author: Agony
 * @create: 2026/5/18 13:22
 * @describe: 提前按场景配好 ChatClient，注入的时候用 @Qualifier 区分
 */
@Configuration
public class MultiModelConfig {

    @Primary
    @Bean("primaryChatClient")
    public ChatClient primaryChatClient(DashScopeChatModel dashScopeChatModel) {

        return ChatClient.builder(dashScopeChatModel)
                .defaultSystem("你是一个专业的助手")
                .build();
    }

    @Bean("backupChatClient")
    public ChatClient backupChatClient(OpenAiChatModel openAiChatModel) {

        return ChatClient.builder(openAiChatModel)
                .defaultSystem("你是要给专业的助手")
                .build();
    }
}