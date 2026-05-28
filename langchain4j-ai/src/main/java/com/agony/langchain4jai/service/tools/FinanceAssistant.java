package com.agony.langchain4jai.service.tools;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * @author: Agony
 * @create: 2026/5/28 12:39
 * @describe:
 */
@AiService
public interface FinanceAssistant {

    @SystemMessage("""
            你是一个金融助手，可以查询股票价格、汇率，并做数学计算。
            遇到需要数据的问题，先调工具获取数据，再给出完整答案。
            """)
    String chat(@UserMessage String message);
}