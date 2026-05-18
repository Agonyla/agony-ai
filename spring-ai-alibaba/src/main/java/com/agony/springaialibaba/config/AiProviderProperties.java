package com.agony.springaialibaba.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/18 14:39
 * @describe: 读取yml切换模型
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.ai")
public class AiProviderProperties {

    // 对应 app.ai.default-provider，没配则默认 qwen
    private String defaultProvider = "qwen";

    // 对应 app.ai.providers，key 是厂商名（deepseek/qwen），value 是该厂商的配置
    private Map<String, ProviderConfig> providers = new HashMap<>();

    public record ProviderConfig(String model) {
    }
}