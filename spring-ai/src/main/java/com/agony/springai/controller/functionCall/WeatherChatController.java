package com.agony.springai.controller.functionCall;

import com.agony.springai.tools.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/9 13:51
 * @describe:
 */
@RestController
@RequestMapping("/api/weather")
public class WeatherChatController {

    private final ChatClient chatClient;

    private final WeatherTool weatherTool;

    public WeatherChatController(ChatClient.Builder builder, WeatherTool weatherTool) {
        this.chatClient = builder.defaultSystem("你是一个天气助手，帮用户查询天气信息，不要编造天气，只返回工具返回的信息回答。").build();
        this.weatherTool = weatherTool;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt().user(message)
                .tools(weatherTool)
                .call()
                .content();
    }
}