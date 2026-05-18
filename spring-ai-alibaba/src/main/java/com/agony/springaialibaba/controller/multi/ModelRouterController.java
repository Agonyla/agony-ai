package com.agony.springaialibaba.controller.multi;

import com.agony.springaialibaba.service.ModelRouterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Agony
 * @create: 2026/5/18 17:49
 * @describe:
 */
@RestController
@RequestMapping("/api/router")
public class ModelRouterController {

    private final ModelRouterService modelRouterService;

    public ModelRouterController(ModelRouterService modelRouterService) {
        this.modelRouterService = modelRouterService;
    }

    /**
     * 按业务场景路由到合适的模型。
     * scene 可选值：code-review / chinese-text / customer-service
     * 不传 scene 默认用 chinese-text
     *
     * @param message
     * @param scene
     * @return
     */
    @GetMapping("/scene")
    public String chatByScene(@RequestParam String message,
                              @RequestParam(defaultValue = "chinese-text") String scene) {

        return modelRouterService.selectForScene(scene)
                .prompt()
                .user(message)
                .call()
                .content();
    }

    /**
     * 带自动降级的调用。
     * provider 可选值：deepseek / qwen，不传默认用配置文件里的 default-provider
     * 如果指定的模型失败，自动切换到另一个，业务层无感知
     *
     * @param message
     * @param provider
     * @return
     */
    @GetMapping("/fallback")
    public String chatWithFallback(
            @RequestParam String message,
            @RequestParam(defaultValue = "qwen") String provider) {

        return modelRouterService.callWithFallback(message, provider);
    }

}