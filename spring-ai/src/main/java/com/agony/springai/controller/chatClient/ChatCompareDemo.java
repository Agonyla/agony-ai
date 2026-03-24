package com.agony.springai.controller.chatClient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

/**
 * @author: Agony
 * @create: 2026/3/15 11:15
 * @describe:
 */
public class ChatCompareDemo {

    private final ChatClient chatClient;

    private final ChatModel chatModel;

    public ChatCompareDemo(ChatClient chatClient, ChatModel chatModel) {
        this.chatClient = chatClient;
        this.chatModel = chatModel;
    }

    public void compare() {

        // use model
        Prompt prompt = new Prompt(new UserMessage("hello"));
        ChatResponse call = chatModel.call(prompt);
        String text1 = call.getResult().getOutput().getText();

        // user client
        String text2 = chatClient.prompt().user("hello").call().content();
    }
}