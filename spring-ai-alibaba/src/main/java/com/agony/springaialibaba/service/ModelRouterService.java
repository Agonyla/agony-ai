package com.agony.springaialibaba.service;

import com.agony.springaialibaba.config.AiProviderProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author: Agony
 * @create: 2026/5/18 15:00
 * @describe:
 */
@Service
public class ModelRouterService {

    private final Map<String, ChatClient> chatClients;

    private final String defaultProvider;

    private final AiProviderProperties properties;

    public ModelRouterService(@Qualifier("primaryChatClient") ChatClient primaryChatClient,
                              @Qualifier("backupChatClient") ChatClient backupChatClient,
                              AiProviderProperties properties) {

        this.properties = properties;
        this.defaultProvider = properties.getDefaultProvider();
        this.chatClients = Map.of(
                "qwen", primaryChatClient,
                "deepseek", backupChatClient
        );
    }

    /**
     * 按业务场景选择模型：不同场景有不同的最优解
     * - code-review：DeepSeek 代码能力强
     * - chinese-text / customer-service：通义千问中文效果好、国内合规
     *
     * @param scene
     * @return
     */
    public ChatClient selectForScene(String scene) {

        return switch (scene) {
            case "code-review" -> chatClients.get("deepseek");
            case "chinese-text", "customer-service" -> chatClients.get("qwen");
            default -> chatClients.getOrDefault(defaultProvider, chatClients.get("qwen"));
        };
    }

    /**
     * 带降级的调用：优先用 preferredProvider，失败后自动切换到另一个。
     * 模型规格（model 名）从 app.ai.providers 配置里读取，改配置不改代码即可切换。
     *
     * @param message
     * @param preferredProvider
     * @return
     */
    public String callWithFallback(String message, String preferredProvider) {
        // 找不到指定厂商时，退回到配置的 defaultProvider
        ChatClient primary = chatClients.getOrDefault(preferredProvider,
                chatClients.get(defaultProvider));

        // 从 providers 配置里读 model 名，动态覆盖 ChatClient 默认的模型规格
        // 例：app.ai.providers.deepseek.model=deepseek-reasoner 即可切到推理模式
        // 如果配置里没有这个 provider 的条目，options 为 null，沿用 ChatClient 默认值
        ChatOptions options = Optional.ofNullable(properties.getProviders().get(preferredProvider))
                .map(cfg -> (ChatOptions) ChatOptions.builder().model(cfg.model()).build())
                .orElse(null);

        try {
            var prompt = primary.prompt().user(message);
            if (options != null) prompt = prompt.options(options); // 有配置就覆盖，没有就不传
            return prompt.call().content();
        } catch (Exception primaryException) {
            // 主模型调用失败（超时、限流、服务不可用等），自动切换到 Map 里的另一个 ChatClient
            ChatClient fallback = chatClients.values().stream()
                    .filter(c -> c != primary)  // 排除刚才失败的那个
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("所有模型均不可用", primaryException));

            // 降级调用不传 options，用备用模型的默认配置
            return fallback.prompt()
                    .user(message)
                    .call()
                    .content();
        }
    }
}