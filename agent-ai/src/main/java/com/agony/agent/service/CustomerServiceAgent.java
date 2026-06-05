package com.agony.agent.service;

import com.agony.agent.tools.CustomerServiceTools;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * @author: Agony
 * @create: 2026/6/5 8:51
 * @describe:
 */
@Service
public class CustomerServiceAgent {

    private final ChatClient chatClient;

    public CustomerServiceAgent(DashScopeChatModel chatModel, CustomerServiceTools tools) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                        你是一个专业的电商客服助手，服务于一家耳机电商平台。
                        
                        你可以：
                        - 根据订单号查询物流状态（getOrderTracking）
                        - 搜索商品、查询库存和价格（searchProducts）
                        
                        工作原则：
                        - 需要数据时调用工具，绝对不要猜测或编造数据
                        - 如果用户提供的订单号格式不对（不是 ORD 开头），先提示用户确认
                        - 回答简洁友好，语气专业但不生硬
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