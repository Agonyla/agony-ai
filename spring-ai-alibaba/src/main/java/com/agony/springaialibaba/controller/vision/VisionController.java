package com.agony.springaialibaba.controller.vision;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
            .model("qwen-vl-plus")          // 必须用视觉模型，qwen-max 不支持图片输入
            .multiModel(true)               // 必须设置，否则请求打到文本端点而非多模态端点，直接报 URL 错误
            .build();

    /**
     * 传图片 URL 分析
     *
     * @param imgUrl
     * @param question
     * @return
     */
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

    /**
     * 上传图片文件分析
     *
     * @param imageFile
     * @param question
     * @return
     */
    @PostMapping("/analyze-upload")
    public String analyzeUploadImage(
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam(defaultValue = "请描述一下这张图片") String question) {

        MimeType mimeType = MimeType.valueOf(imageFile.getContentType() != null ? imageFile.getContentType() : "image/jpeg");

        Media media = Media.builder()
                .mimeType(mimeType)
                .data(imageFile.getResource())
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

    /**
     * 多张图片对比
     *
     * @param images
     * @param question
     * @return
     */
    @PostMapping("/compare-images")
    public String compareImages(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam String question) throws Exception {

        List<Media> mediaList = new ArrayList<>();
        for (MultipartFile image : images) {
            MimeType mimeType = MimeType.valueOf(
                    image.getContentType() != null ? image.getContentType() : "image/jpeg");

            mediaList.add(Media.builder()
                    .mimeType(mimeType)
                    .data(image.getResource())
                    .build());
        }

        UserMessage message = UserMessage.builder()
                .text(question)
                .media(mediaList)
                .build();

        return chatClient.prompt()
                .messages(message)
                .options(VL_OPTIONS)
                .call()
                .content();
    }
}