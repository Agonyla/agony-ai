package com.agony.springai.controller.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

/**
 * @author: Agony
 * @create: 2026/5/9 11:25
 * @describe: WebFlux Stream
 */
@RestController
@RequestMapping("/api/stream")
public class WebFluxStreamController {

    private static final Logger log = LoggerFactory.getLogger(WebFluxStreamController.class);

    private final ChatClient chatClient;

    public WebFluxStreamController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * WebFlux版本
     *
     * @param message
     * @return
     */
    @GetMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content()
                .doOnNext(System.out::println)
                .doOnComplete(() -> System.out.println("\n完成"));
    }

    public Flux<String> streamChat(@RequestParam String message) {

        return chatClient.prompt().user(message)
                .stream()
                .content()
                .timeout(Duration.ofSeconds(30))
                // 超时控制：30 秒内没有新数据就触发 TimeoutException
                .onErrorResume(TimeoutException.class, e -> Flux.just("[响应超时，请重试]"))
                // 其他异常统一处理
                .onErrorResume(e -> {
                    log.error("流式调用出错: {}", e.getMessage());
                    return Flux.just("[抱歉，生成过程中出现错误，请稍后重试]");
                });

    }
}