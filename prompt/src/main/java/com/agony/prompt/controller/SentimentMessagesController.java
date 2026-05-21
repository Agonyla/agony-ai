package com.agony.prompt.controller;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/21 15:03
 * @describe: 用 Messages 列表模拟对话历史（更接近模型训练格式）
 */
@RestController
@RequestMapping("/api/sentiment-messages")
public class SentimentMessagesController {

    private final ChatClient chatClient;

    public SentimentMessagesController(DashScopeChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    @GetMapping
    public String analyzeWithMessages(@RequestParam String comment) {

        List<Message> messages = new ArrayList<>();

        messages.add(new SystemMessage("对用户评论进行情感分析，输出 POSITIVE/NEGATIVE/NEUTRAL。"));

        // 示例对话（模拟历史对话格式）
        messages.add(new UserMessage("物流很快，东西也不错，就是包装有点简单"));
        messages.add(new AssistantMessage("POSITIVE"));

        messages.add(new UserMessage("快递慢，客服态度差，商品也有破损"));
        messages.add(new AssistantMessage("NEGATIVE"));

        messages.add(new UserMessage("和描述一致，正常收到，没什么特别的"));
        messages.add(new AssistantMessage("NEUTRAL"));

        messages.add(new UserMessage(comment));

        return chatClient.prompt()
                .messages(messages)
                .call()
                .content()
                .trim();
    }

}