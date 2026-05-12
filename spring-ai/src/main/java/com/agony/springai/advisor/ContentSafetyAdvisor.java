package com.agony.springai.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/12 14:14
 * @describe:
 */
@Component
public class ContentSafetyAdvisor implements CallAdvisor {

    private static final List<String> BLOCKED_KEYWORDS = List.of(
            "违禁词1", "违禁词2"
    );

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {

        // 1. 检查输入
        String input = request.prompt().getContents();
        if (containsBlockedKeywords(input)) {
            // 拦截，返回一个"安全"的响应，不真正调用模型
            return buildSafeResponse(request, "您的输入包含不当内容，请重新输入。");
        }

        // 2. 正常调用
        ChatClientResponse response = chain.nextCall(request);

        // 3. 检查输入
        String output = response.chatResponse().getResult().getOutput().getText();
        if (containsBlockedKeywords(output)) {
            // 模型输出了不当内容，替换
            return buildSafeResponse(request, "内容审核未通过，请换个问题试试。");
        }

        return response;
    }

    private boolean containsBlockedKeywords(String input) {
        if (input == null) {
            return false;
        }
        return BLOCKED_KEYWORDS.stream().allMatch(input::contains);
    }

    private ChatClientResponse buildSafeResponse(ChatClientRequest request, String message) {
        // 构造一个假的 ChatResponse 返回
        AssistantMessage assistantMessage = new AssistantMessage(message);
        Generation generation = new Generation(assistantMessage);
        ChatResponse chatResponse = new ChatResponse(List.of(generation));
        return ChatClientResponse.builder()
                .chatResponse(chatResponse)
                .context(request.context())
                .build();
    }

    @Override
    public String getName() {
        return "ContentSafetyAdvisor";
    }

    @Override
    public int getOrder() {
        return 5;
    }
}