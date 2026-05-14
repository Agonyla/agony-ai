package com.agony.springaialibaba.controller.vision;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @author: Agony
 * @create: 2026/5/14 11:21
 * @describe:
 */
@RestController
@RequestMapping("/api/vision")
public class VisionController {

    private final ChatClient chatClient;

    public VisionController(DashScopeChatModel dashScopeChatModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
    }

    // 视觉模型 Options，三个接口都要用
    private static final DashScopeChatOptions VL_OPTIONS = DashScopeChatOptions.builder()
            .model("qwen-vl-puls")          // 必须用视觉模型，qwen-max 不支持图片输入
            .multiModel(true)               // 必须设置，否则请求打到文本端点而非多模态端点，直接报 URL 错误
            .build();

    @GetMapping("/analyze-url")
    public String analyzeImage(
            @RequestParam String imgUrl,
            @RequestParam(defaultValue = "请描述一下这张图片") String question) {

        Media media = Media.builder()
                .mimeType(MimeTypeUtils.IMAGE_JPEG)
                .data(URI.create(imgUrl))
                .build();

        UserMessage message = UserMessage.builder()
                .text(question)
                .media(media)
                .build();

        return chatClient.prompt()
                .messages(message)
                .options(VL_OPTIONS)
                .call()
                .content();
    }
}