package com.agony.langchain4jai.service.dynamic;

import com.agony.langchain4jai.model.UserRole;
import com.agony.langchain4jai.tools.dynamic.AdminTools;
import com.agony.langchain4jai.tools.dynamic.ModifyTools;
import com.agony.langchain4jai.tools.dynamic.QueryTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/28 14:11
 * @describe:
 */
@Service
public class DynamicAgentService {

    private final ChatModel chatModel;

    private final QueryTools queryTools;
    private final ModifyTools modifyTools;
    private final AdminTools adminTools;

    public DynamicAgentService(
            ChatModel chatModel,
            QueryTools queryTools,
            ModifyTools modifyTools,
            AdminTools adminTools) {

        this.chatModel = chatModel;
        this.queryTools = queryTools;
        this.modifyTools = modifyTools;
        this.adminTools = adminTools;
    }

    public String chat(String sessionId, UserRole role, String message) {

        List<Object> tools = buildToolSet(role);

        DynamicAssistant assistant = AiServices.builder(DynamicAssistant.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .tools(tools.toArray())
                .build();

        return assistant.chat(sessionId, message);
    }

    private List<Object> buildToolSet(UserRole role) {

        List<Object> tools = new ArrayList<>();

        tools.add(unwrap(queryTools));

        if (role == UserRole.ADMIN || role == UserRole.MEMBER) {
            tools.add(unwrap(modifyTools));
        }

        if (role == UserRole.ADMIN) {
            tools.add(unwrap(adminTools));
        }

        return tools;
    }

    /**
     * 剥离 Spring AOP CGLIB 代理，拿到真实对象。
     * 若项目里有 @Aspect 拦截了 @Tool 方法（如统一异常处理），工具类会被代理，
     * LangChain4j 扫描不到 @Tool 注解，必须先解包再传入。
     */
    private Object unwrap(Object bean) {

        try {
            return AopUtils.isAopProxy(bean) ? ((Advised) bean).getTargetSource().getTarget() : bean;
        } catch (Exception e) {
            return bean;
        }
    }
}