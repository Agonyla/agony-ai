package com.agony.langchain4jai.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

/**
 * @author: Agony
 * @create: 2026/5/28 12:38
 * @describe:
 */
@Component
public class StockTools {

    @Tool("查询股票实时价格，返回股票代码、当前价格和涨跌幅")
    public String getStockPrice(@P("股票代码，例如 AAPL、TSLA、000001.SZ") String symbol) {
        return String.format("{\"symbol\":\"%s\",\"price\":200.42,\"change\":\"+1.2%%\"}", symbol);
    }
}