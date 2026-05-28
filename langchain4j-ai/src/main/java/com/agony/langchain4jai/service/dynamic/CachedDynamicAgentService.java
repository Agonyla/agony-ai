package com.agony.langchain4jai.service.dynamic;

import com.agony.langchain4jai.model.UserRole;
import com.agony.langchain4jai.tools.dynamic.AdminTools;
import com.agony.langchain4jai.tools.dynamic.ModifyTools;
import com.agony.langchain4jai.tools.dynamic.QueryTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Agony
 * @create: 2026/5/28 16:19
 * @describe:
 */
@Service
public class CachedDynamicAgentService {

    private final ChatModel chatModel;

    private final QueryTools queryTools;
    private final ModifyTools modifyTools;
    private final AdminTools adminTools;

    private final Map<UserRole, DynamicAssistant> agentCache = new EnumMap<>(UserRole.class);

    public CachedDynamicAgentService(
            ChatModel chatModel,
            QueryTools queryTools,
            ModifyTools modifyTools,
            AdminTools adminTools) {

        this.chatModel = chatModel;
        this.queryTools = queryTools;
        this.modifyTools = modifyTools;
        this.adminTools = adminTools;
    }

    @PostConstruct
    public void init() {
        agentCache.put(UserRole.GUEST, buildAgent(UserRole.GUEST));
        agentCache.put(UserRole.MEMBER, buildAgent(UserRole.MEMBER));
        agentCache.put(UserRole.ADMIN, buildAgent(UserRole.ADMIN));
    }

    public String chat(String sessionId, UserRole role, String message) {

        return agentCache.get(role).chat(sessionId, message);
    }

    private DynamicAssistant buildAgent(UserRole role) {

        List<Object> tools = new ArrayList<>();

        tools.add(unwrap(queryTools));

        if (role == UserRole.ADMIN || role == UserRole.MEMBER) {
            tools.add(unwrap(modifyTools));
        }

        if (role == UserRole.ADMIN) {
            tools.add(unwrap(adminTools));
        }

        return AiServices.builder(DynamicAssistant.class)
                .chatModel(chatModel)
                .tools(tools.toArray())
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(10))
                .build();
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