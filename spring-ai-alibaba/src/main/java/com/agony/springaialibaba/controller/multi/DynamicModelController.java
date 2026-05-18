package com.agony.springaialibaba.controller.multi;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/18 14:23
 * @describe: 根据请求参数决定用哪个模型
 */
@RestController
@RequestMapping("/api/dynamic")
public class DynamicModelController {

    private final Map<String, ChatModel> chatModels;

    public DynamicModelController(OpenAiChatModel openAiChatModel, DashScopeChatModel dashScopeChatModel) {

        this.chatModels = Map.of(
                "qwen", dashScopeChatModel,
                "deepseek", openAiChatModel);
    }

    @GetMapping("/chat")
    public String chat(
            @RequestParam String message,
            @RequestParam(defaultValue = "qwen") String provider) {

        ChatModel chatModel = chatModels.getOrDefault(provider, chatModels.get("qwen"));

        return ChatClient.builder(chatModel)
                .build()
                .prompt()
                .user(message)
                .call()
                .content();
    }
}