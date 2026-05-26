package com.agony.prompt.service;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/26 11:11
 * @describe:
 */
@Service
public class CodeReviewService {

    private final ChatClient chatClient;

    @Value("classpath:prompts/code-review.st")
    private Resource codeReviewPromptResource;

    public CodeReviewService(DashScopeChatModel dashScopeChatModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
    }

    public String review(String code, String language) {

        PromptTemplate pt = new PromptTemplate(codeReviewPromptResource);

        String userPrompt = pt.render(Map.of(
                "language", language,
                "code", code
        ));

        return chatClient.prompt()
                .system("""
                        你是一个资深工程师，专注代码质量。
                        找出 Bug、性能问题和最佳实践违反，每个问题标注严重程度。""")
                .user(userPrompt)
                .call()
                .content();
    }
}