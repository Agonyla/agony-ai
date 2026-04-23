package com.agony.springai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/4/23 15:04
 * @describe:
 */
@RestController
@RequestMapping("/issue")
public class IssueController {

    private final ChatClient chatClient;

    public IssueController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    enum Priority {
        CRITICAL,
        HIGH,
        MEDIUM,
        LOW
    }

    enum Category {
        BUG,
        FEATURE,
        IMPROVEMENT,
        DOCUMENTATION
    }

    record IssueClassification(
            String title,
            Category category,
            Priority priority,
            String assignTo,
            String reason
    ) {
    }

    record IssueRequest(String description) {
    }

    @PostMapping("/classify")
    public IssueClassification classify(@RequestBody IssueRequest request) {
        return chatClient.prompt()
                .system("你是项目经理，负责对 Issue 进行分类和优先级评估。")
                .user("请对这个 Issue 进行分类：" + request.description())
                .call()
                .entity(IssueClassification.class);
    }
}