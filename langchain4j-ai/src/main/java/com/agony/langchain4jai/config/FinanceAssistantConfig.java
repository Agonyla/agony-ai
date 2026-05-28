package com.agony.langchain4jai.config;

import com.agony.langchain4jai.service.tools.FinanceAssistant;
import com.agony.langchain4jai.tools.CalculatorTools;
import com.agony.langchain4jai.tools.CurrencyTools;
import com.agony.langchain4jai.tools.StockTools;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author: Agony
 * @create: 2026/5/28 12:39
 * @describe:
 */
@Configuration
public class FinanceAssistantConfig {

    @Bean
    @Primary
    public FinanceAssistant financeAssistantWithTools(ChatModel model,
                                                      StockTools stockTools,
                                                      CurrencyTools currencyTools,
                                                      CalculatorTools calculatorTools) {
        return AiServices.builder(FinanceAssistant.class)
                .chatModel(model)
                .tools(stockTools, currencyTools, calculatorTools)
                .build();
    }
}