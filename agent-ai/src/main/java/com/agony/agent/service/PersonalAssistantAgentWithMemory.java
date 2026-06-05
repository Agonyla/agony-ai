package com.agony.agent.service;

import com.agony.agent.tools.AssistantTools;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.stereotype.Service;

/**
 * @author: Agony
 * @create: 2026/6/5 9:57
 * @describe:
 */
@Service
public class PersonalAssistantAgentWithMemory {

    private final ChatClient chatClient;

    public PersonalAssistantAgentWithMemory(DashScopeChatModel chatModel,
                                            AssistantTools assistantTools) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                        你是一个聪明的个人助理，名字叫小智。
                        能查天气、告知时间、查汇率、创建提醒。
                        需要数据时调工具，不要猜测和编造。
                        """)
                .defaultTools(assistantTools)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(
                                        MessageWindowChatMemory.builder()
                                                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                                                .build())
                                .build())
                .build();
    }

    public String chat(String message, String sessionId) {
        return chatClient.prompt()
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .call()
                .content();
    }
}