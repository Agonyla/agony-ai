package com.agony.springai.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author: Agony
 * @create: 2026/5/12 12:58
 * @describe:
 */
@Component
public class LoggingAdvisor implements CallAdvisor, StreamAdvisor {

    private static final Logger log = LoggerFactory.getLogger(LoggingAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {

        long start = System.currentTimeMillis();

        // 打印请求（getContents() 返回所有消息拼接后的文本）
        String contents = request.prompt().getContents();
        log.info("[AI调用] 用户消息: {}", contents);

        // 继续执行链（调用下一个 Advisor 或最终调用模型）
        ChatClientResponse response = chain.nextCall(request);

        // 打印响应
        String result = response.chatResponse().getResult().getOutput().getText();

        log.info("[AI调用] 模型回复 ({}ms): {}",
                System.currentTimeMillis() - start,
                result.length() > 100 ? result.substring(0, 100) + "..." : result);

        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {

        long start = System.currentTimeMillis();
        String contents = request.prompt().getContents();
        log.info("[AI流式调用] 用户消息: {}", contents);

        return chain.nextStream(request)
                .doOnComplete(() -> log.info("[AI流式调用] 完成，耗时 {}ms",
                        System.currentTimeMillis() - start));
    }

    @Override
    public String getName() {
        return "LoggingAdvisor";
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}