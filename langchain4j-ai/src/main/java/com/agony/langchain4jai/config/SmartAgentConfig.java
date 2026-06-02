package com.agony.langchain4jai.config;

import com.agony.langchain4jai.service.agent.SmartAgent;
import com.agony.langchain4jai.tools.agent.ArithmeticMathTools;
import com.agony.langchain4jai.tools.agent.CityWeatherTools;
import com.agony.langchain4jai.tools.agent.WebSearchTools;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Agony
 * @create: 2026/6/2 10:13
 * @describe:
 */
@Configuration
public class SmartAgentConfig {

    @Bean
    public SmartAgent smartAgent(
            ChatModel chatModel,
            ArithmeticMathTools arithmeticMathTools,
            CityWeatherTools cityWeatherTools,
            WebSearchTools webSearchTools
    ) {

        return AiServices.builder(SmartAgent.class)
                .chatModel(chatModel)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(20))
                .tools(unwrap(arithmeticMathTools), unwrap(cityWeatherTools), unwrap(webSearchTools))
                .build();
    }

    private Object unwrap(Object bean) {
        if (AopUtils.isAopProxy(bean) && bean instanceof Advised advised) {
            try {
                return advised.getTargetSource().getTarget();
            } catch (Exception e) {
                throw new RuntimeException("无法解包 AOP 代理：" + bean.getClass(), e);
            }
        }
        return bean;
    }
}