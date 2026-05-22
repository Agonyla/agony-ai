package com.agony.prompt.controller;

import com.agony.prompt.entity.ReviewAnalysis;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/22 16:00
 * @describe:
 */
@RestController
@RequestMapping("/structured")
public class StableStructuredOutputController {

    private static final String SYSTEM = """
            你是一个用户评论分析专家。
            分析评论的情感倾向、优缺点和整体评分。
            
            输出规则（严格遵守）：
            - 只输出合法的 JSON，第一个字符必须是 {，最后一个字符必须是 }
            - 禁止在 JSON 前后添加任何文字、解释或 Markdown 代码块
            - sentiment 只能是 POSITIVE / NEGATIVE / MIXED / NEUTRAL 之一
            - 列表为空时填 []，不要填 null
            - overallScore 范围 1-10 的整数
            """;

    private final ChatClient chatClient;
    private final BeanOutputConverter<ReviewAnalysis> converter;

    public StableStructuredOutputController(DashScopeChatModel dashScopeChatModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
        this.converter = new BeanOutputConverter<>(ReviewAnalysis.class);
    }

    @PostMapping("/analyze-review")
    public ReviewAnalysis analyzeReview(@RequestBody String review) {

        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .temperature(0.0)
                .build();

        String raw = chatClient.prompt()
                .system(SYSTEM + "\n\n" + converter.getFormat())
                .user("分析这条评论" + review)
                .options(options)
                .call()
                .content();

        return converter.convert(raw);
    }
}