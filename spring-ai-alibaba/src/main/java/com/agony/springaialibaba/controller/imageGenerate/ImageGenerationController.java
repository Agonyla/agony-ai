package com.agony.springaialibaba.controller.imageGenerate;

import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Agony
 * @create: 2026/5/18 9:27
 * @describe: 基础图片生成
 */
@RestController
@RequestMapping("/api/image")
public class ImageGenerationController {

    private final ImageModel imageModel;

    public ImageGenerationController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @GetMapping("/generate")
    public String generateImage(@RequestParam String description) {

        ImageResponse response = imageModel.call(new ImagePrompt(description));

        return response.getResult().getOutput().getUrl();
    }

    @GetMapping("/generate-custom")
    public List<String> generateCustomImage(
            @RequestParam String description,
            @RequestParam(defaultValue = "1") int count,
            @RequestParam(defaultValue = "1024") int width,
            @RequestParam(defaultValue = "1024") int height) {

        ImageResponse response = imageModel.call(
                new ImagePrompt(
                        description,
                        DashScopeImageOptions.builder()
                                .model("wanx2.1-t2i-plus")
                                .n(count)
                                .width(width)
                                .height(height)
                                .build()));

        return response.getResults().stream()
                .map(result -> result.getOutput().getUrl())
                .collect(Collectors.toList());
    }
}