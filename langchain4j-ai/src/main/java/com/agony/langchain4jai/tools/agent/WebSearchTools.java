package com.agony.langchain4jai.tools.agent;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

/**
 * @author: Agony
 * @create: 2026/6/2 10:12
 * @describe:
 */
@Component
public class WebSearchTools {

    @Tool("搜索互联网获取信息，适用于查询实时新闻、百科知识等")
    public String search(@P("搜索关键词") String query) {
        return "搜索结果：关于 " + query + " 的相关信息：这是一段模拟的搜索结果内容。";
    }
}