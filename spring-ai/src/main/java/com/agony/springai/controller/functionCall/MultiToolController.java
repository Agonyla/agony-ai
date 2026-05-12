package com.agony.springai.controller.functionCall;

import com.agony.springai.tools.WeatherTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/12 10:30
 * @describe: 演示多工具 + 多次调用：
 * 同时注册天气查询和天气预报两个工具，
 * 用户一句话可能触发多次工具调用
 */
@RestController
@RequestMapping("/api/multi-tool")
public class MultiToolController {

    private final ChatClient chatClient;
    private final WeatherTool weatherTools;

    public MultiToolController(ChatClient.Builder builder, WeatherTool weatherTools) {
        this.weatherTools = weatherTools;
        this.chatClient = builder
                .defaultSystem("你是一个天气助手，可以查询实时天气和天气预报。不要编造数据。")
                .build();
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .tools(weatherTools)   // 注册了 getWeather 和 getWeatherForecast 两个工具
                .call()
                .content();
    }

}