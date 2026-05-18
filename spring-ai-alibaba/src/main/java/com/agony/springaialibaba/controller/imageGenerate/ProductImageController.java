package com.agony.springaialibaba.controller.imageGenerate;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/18 9:44
 * @describe: 商品配图生成
 */
@RestController
@RequestMapping("/api/product-image")
public class ProductImageController {

    private final ChatClient chatClient;

    private final ImageModel imageModel;

    public ProductImageController(DashScopeChatModel dashScopeChatModel, ImageModel imageModel) {
        this.chatClient = ChatClient.builder(dashScopeChatModel).build();
        this.imageModel = imageModel;
    }

    record GenerateRequest(
            String productName,
            String productDescription,
            String style
    ) {
    }

    record GenerateResult(
            String imagePrompt,
            String imageUrl
    ) {
    }

    @PostMapping("generate")
    public GenerateResult generateProductImage(@RequestBody GenerateRequest generateRequest) {
        String imagePrompt = chatClient.prompt()
                .system("""
                        你是一个专业的 AI 绘画 prompt 工程师。
                        根据商品信息，生成一段用于 AI 图片生成的英文 prompt。
                        要求：用英文写，50-100 词，包含商品外观特征、背景、光线、风格，
                        商业摄影风格，适合电商展示。只输出 prompt 文本，不要其他内容。""")
                .user(String.format(
                        "商品名称：%s\n商品描述：%s\n风格要求：%s",
                        generateRequest.productName(),
                        generateRequest.productDescription(),
                        generateRequest.style()))
                .call()
                .content();

        ImageResponse response = imageModel.call(
                new ImagePrompt(
                        imagePrompt,
                        DashScopeImageOptions.builder()
                                .model("wanx2.1-t2i-plus")
                                .n(1)
                                .width(1024)
                                .height(1024)
                                .build()));

        return new GenerateResult(imagePrompt, response.getResult().getOutput().getUrl());
    }
}