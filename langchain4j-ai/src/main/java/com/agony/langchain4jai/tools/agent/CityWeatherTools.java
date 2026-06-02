package com.agony.langchain4jai.tools.agent;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

/**
 * @author: Agony
 * @create: 2026/6/2 10:09
 * @describe:
 */
@Component
public class CityWeatherTools {

    @Tool("查询城市实时天气，返回天气状况和温度")
    public String getWeather(@P("城市名称") String city) {
        return city + ": 晴天，18℃，风力2级";
    }
}