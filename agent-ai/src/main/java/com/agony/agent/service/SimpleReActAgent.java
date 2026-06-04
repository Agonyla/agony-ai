package com.agony.agent.service;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/6/4 15:29
 * @describe:
 */
@Service
public class SimpleReActAgent {

    @FunctionalInterface
    interface AgentTool {
        String execute(String input);
    }

    private final ChatClient chatClient;

    private final Map<String, AgentTool> agentTools;

    public SimpleReActAgent(DashScopeChatModel dashScopeChatModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
        this.agentTools = Map.of(
                "getWeather", this::getWeather,  // input -> getWeather(input)
                "getDate", this::getDate
        );

    }

    /**
     * 执行 ReAct 循环
     *
     * @param userTask     用户任务描述
     * @param maxIteration 最大循环次数（防止死循环）
     * @return 输出
     */
    public String run(String userTask, int maxIteration) {

        List<Message> messages = new ArrayList<>();
        String systemPrompt = buildSystemPrompt();

        messages.add(new UserMessage(userTask));

        for (int i = 0; i < maxIteration; i++) {

            String modelOutput = chatClient.prompt()
                    .system(systemPrompt)
                    .messages(messages)
                    .call()
                    .content();

            messages.add(new AssistantMessage(modelOutput));

            System.out.println("=== 第 " + (i + 1) + " 轮 ===");
            System.out.println(modelOutput);

            // 检测是否输出了最终答案
            if (modelOutput.contains("Final Answer:")) {
                return modelOutput;
            }

            // 解析模型想调用哪个工具、参数是什么
            String toolName = extractAction(modelOutput);
            String toolInput = extractActionInput(modelOutput);

            if (toolName == null) {
                return "模型输出格式异常，无法继续执行";
            }

            AgentTool tool = agentTools.get(toolName);
            String observation;

            if (tool == null) {
                observation = "工具 " + toolName + " 不存在，请换一个";
            } else {
                observation = tool.execute(toolInput);
            }

            System.out.println("Observation: " + observation);

            // 把观察结果加回消息历史，下一轮模型就能看到这个结果
            messages.add(new UserMessage("Observation: " + observation));
        }

        return "超过最大迭代次数（" + maxIteration + "），任务未完成";
    }

    // 解析工具

    /**
     * 提取最终回答
     *
     * @param output
     * @return
     */
    private String extractFinalAnswer(String output) {

        String finalAnswer = "Final Answer:";

        int idx = output.indexOf(finalAnswer);
        if (idx == -1) return output;
        return output.substring(idx + finalAnswer.length()).strip();
    }

    /**
     * 提取工具名称
     *
     * @param output
     * @return
     */
    private String extractAction(String output) {
        for (String line : output.split("\n")) {
            if (line.startsWith("Action:")) {
                return line.substring("Action:".length()).strip();
            }
        }
        return null;
    }

    /**
     * 提取工具输入格式
     *
     * @param output
     * @return
     */
    private String extractActionInput(String output) {
        for (String line : output.split("\n")) {
            if (line.startsWith("Action Input:")) {
                return line.substring("Action Input:".length()).strip();
            }
        }
        return "";
    }

    /**
     * build system prompt
     *
     * @return system prompt
     */
    private String buildSystemPrompt() {
        return """
                你是一个智能助手，按照以下格式严格输出，每次只做一个动作：
                
                可用工具：
                - getWeather(input: JSON {"city": "城市名", "date": "today/tomorrow"})：查询天气
                - getDate(input: 无)：获取今天的日期
                
                输出格式（严格遵守）：
                Thought: [你的分析和下一步计划]
                Action: [工具名]
                Action Input: [工具参数，JSON 格式]
                
                收到 Observation 后继续思考，直到可以回答为止：
                Thought: [分析观察结果]
                Final Answer: [给用户的最终回答]
                
                注意：
                - 每次只输出一个 Action 或 Final Answer，不要一次输出多个
                - 工具名必须和上面列表完全一致
                """;
    }

    // ---- 工具实现 ----

    /**
     * 获取天气
     *
     * @param input
     * @return
     */
    private String getWeather(String input) {
        // 真实项目里这里调用天气 API，这里先 mock
        return """
                {"city": "上海", "weather": "晴", "temp": "8~15°C", "wind": "北风3级"}
                """;
    }

    /**
     * 获取时间
     *
     * @param input
     * @return
     */
    private String getDate(String input) {
        return java.time.LocalDate.now().toString();
    }

}