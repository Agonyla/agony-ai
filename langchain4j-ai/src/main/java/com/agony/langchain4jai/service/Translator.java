package com.agony.langchain4jai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

/**
 * @author: Agony
 * @create: 2026/5/27 15:32
 * @describe:
 */
@AiService
public interface Translator {

    @SystemMessage("你是一个专业翻译")
    @UserMessage("讲一下文字翻译成{{language}}: \n {{text}}")
    String translate(@V("language") String language, @V("text") String text);
}