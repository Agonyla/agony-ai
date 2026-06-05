package com.agony.agent.tools;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Agony
 * @create: 2026/6/5 13:59
 * @describe:
 */
@Component
public class RollingMemoryCompressor {

    private static final int COMPRESS_THRESHOLD = 20; // 超过 20 条时触发压缩

    private static final int KEEP_RECENT = 6;         // 压缩后保留最近 6 条原始消息

    private final ChatClient summaryClient;

    public RollingMemoryCompressor(DashScopeChatModel dashScopeChatModel) {
        this.summaryClient = ChatClient.builder(dashScopeChatModel).build();
    }

    /**
     * 检查消息列表是否需要压缩，需要则执行，返回压缩后的列表
     *
     * @param messages 消息列表
     * @return 压缩后的消息列表
     */
    public List<Message> maybeCompress(List<Message> messages) {

        if (messages.size() < COMPRESS_THRESHOLD) return messages;

        List<Message> toCompress = messages.subList(0, messages.size() - KEEP_RECENT);
        List<Message> toKeep = messages.subList(messages.size() - KEEP_RECENT, messages.size());

        String summary = summary(toCompress);

        List<Message> compressed = new ArrayList<>();

        // 用摘要替换旧消息，放在消息历史的最前面
        compressed.add(new SystemMessage(
                "以下是此前对话的摘要（部分历史已被压缩）：\n" + summary));
        compressed.addAll(toKeep);

        return compressed;
    }

    /**
     * 压缩历史消息
     *
     * @param messages 历史消息
     * @return 摘要
     */
    public String summary(List<Message> messages) {

        StringBuilder history = new StringBuilder();
        messages.forEach(message -> {
            String role = message instanceof SystemMessage ? "系统" :
                    message instanceof UserMessage ? "用户" : "助手";
            history.append(role).append(": ").append(message.getText()).append("\n");
        });

        return summaryClient.prompt()
                .system("""
                        请把以下对话历史压缩成一段简洁的摘要。
                        保留：任务目标、关键决策、重要数据、用户明确表达的偏好和约束。
                        丢弃：闲聊、重复内容、过程中的细节推理。
                        摘要用第三人称，限 200 字以内。
                        """)
                .user(history.toString())
                .call()
                .content();
    }

}