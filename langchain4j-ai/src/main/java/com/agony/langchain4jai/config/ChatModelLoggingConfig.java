package com.agony.langchain4jai.config;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Agony
 * @create: 2026/6/2 10:39
 * @describe:
 */
@Configuration
public class ChatModelLoggingConfig {

    @Bean
    public ChatModelListener loggingListener() {
        return new ChatModelListener() {

            @Override
            public void onRequest(ChatModelRequestContext ctx) {
                System.out.println("\n===== 【发送给模型】 =====");
                ctx.chatRequest().messages().forEach(m ->
                        System.out.println("[" + m.type() + "] " + m));
                if (ctx.chatRequest().toolSpecifications() != null) {
                    System.out.println("可用工具：" +
                            ctx.chatRequest().toolSpecifications().stream()
                                    .map(t -> t.name())
                                    .toList());
                }
            }

            @Override
            public void onResponse(ChatModelResponseContext ctx) {
                System.out.println("\n===== 【模型返回】 =====");
                var response = ctx.chatResponse().aiMessage();
                if (response.text() != null) {
                    System.out.println("回答：" + response.text());
                }
                if (response.hasToolExecutionRequests()) {
                    response.toolExecutionRequests().forEach(t ->
                            System.out.println("调用工具：" + t.name() + "，参数：" + t.arguments()));
                }
            }

            @Override
            public void onError(ChatModelErrorContext ctx) {
                System.out.println("===== 【模型报错】 =====");
                ctx.error().printStackTrace();
            }
        };
    }
}