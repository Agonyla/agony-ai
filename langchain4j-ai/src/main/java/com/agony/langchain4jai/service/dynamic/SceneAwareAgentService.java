package com.agony.langchain4jai.service.dynamic;

import com.agony.langchain4jai.tools.dynamic.AfterSaleTools;
import com.agony.langchain4jai.tools.dynamic.ComplaintTools;
import com.agony.langchain4jai.tools.dynamic.PreSaleTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/28 16:36
 * @describe:
 */
@Service
public class SceneAwareAgentService {

    interface SceneAssistant {
        String chat(@MemoryId String sessionId, @UserMessage String message);
    }

    private final ChatModel chatModel;

    private final Map<String, List<Object>> sceneToolsMap;

    public SceneAwareAgentService(ChatModel chatModel,
                                  PreSaleTools preSaleTools,
                                  AfterSaleTools afterSaleTools,
                                  ComplaintTools complaintTools) throws Exception {

        this.chatModel = chatModel;
        this.sceneToolsMap = Map.of(
                "pre_sale", List.of(unwrap(preSaleTools)),
                "after_sale", List.of(unwrap(afterSaleTools)),
                "complaint", List.of(unwrap(complaintTools))
        );
    }

    private Object unwrap(Object bean) throws Exception {

        return AopUtils.isAopProxy(bean)
                ? ((Advised) bean).getTargetSource().getTarget()
                : bean;
    }

    public String chat(String sessionId, String scene, String message) {

        List<Object> tools = sceneToolsMap.getOrDefault(scene, List.of());
        String systemPrompt = getSystemPromptForScene(scene);

        SceneAssistant assistant = AiServices.builder(SceneAssistant.class)
                .chatModel(chatModel)
                .tools(tools.toArray())
                .systemMessageProvider(memoryId -> systemPrompt)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();

        return assistant.chat(sessionId, message);
    }

    private String getSystemPromptForScene(String scene) {

        return switch (scene) {
            case "pre_sale" -> "你是售前顾问，帮助用户了解和选购商品，重点介绍商品优势";
            case "after_sale" -> "你是售后专员，处理退换货和物流问题，以解决问题为首要目标";
            case "complaint" -> "你是投诉处理专员，态度温和耐心，尽快帮用户创建工单并给出解决方案";
            default -> "你是一个通用客服助手";
        };

    }

}