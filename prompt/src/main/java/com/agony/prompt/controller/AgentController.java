package com.agony.prompt.controller;

import com.agony.prompt.tools.CalculatorTools;
import com.agony.prompt.tools.StockTools;
import com.agony.prompt.tools.WeatherTools;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/25 18:48
 * @describe:
 */
@RestController
@RequestMapping("/agent")
public class AgentController {

    private final ChatClient chatClient;

    public AgentController(DashScopeChatModel dashScopeChatModel,
                           CalculatorTools calculatorTools,
                           StockTools stockTools,
                           WeatherTools weatherTools) {

        this.chatClient = ChatClient.builder(dashScopeChatModel)
                .defaultSystem("""
                        你是一个智能助手，可以查天气、查股价、做计算。
                        根据用户问题决定是否需要使用工具，使用工具后结合结果给出准确答案。
                        """)
                .defaultTools(weatherTools, calculatorTools, stockTools)
                .build();
    }

    @GetMapping
    public String ask(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }
}