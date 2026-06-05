package com.agony.agent.service;

import com.agony.agent.tools.AssistantTools;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * @author: Agony
 * @create: 2026/6/5 9:34
 * @describe:
 */
@Service
public class PersonalAssistantAgent {

    private final ChatClient chatClient;

    public PersonalAssistantAgent(DashScopeChatModel dashScopeChatModel, AssistantTools tools) {
        this.chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem("""
                        你是一个聪明的个人助理，名字叫小智。
                        
                        你可以：
                        - 查询任意城市的实时天气
                        - 告知当前日期、时间和星期
                        - 查询外汇汇率
                        - 帮用户创建提醒事项
                        
                        工作原则：
                        - 需要数据时主动调用工具，不要猜测或编造任何数据
                        - 回答简洁，重点突出，不要废话
                        - 用户一次问多个问题时，把所有相关工具都调完再统一回答
                        - 工具调用失败时，如实告知用户并说明原因
                        """)
                .defaultTools(tools)
                .build();
    }

    public String chat(String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

}