package com.agony.springai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author: Agony
 * @create: 2026/5/9 13:38
 * @describe: 天气工具类
 */
@Component
public class WeatherTool {

    @Tool(description = "获取指定城市的天气。返回温度、天气状况、风力等信息")
    public String getWeather(@ToolParam(description = "城市名称，例如：北京、上海、绍兴") String city) {

        // 假数据
        return String.format("""
                城市：%s
                温度：25°C
                天气：晴
                风力：北风3级
                湿度：45%%
                更新时间：%s""", city, LocalDateTime.now());
    }

    @Tool(description = "获取未来几天的天气预报")
    public String getWeatherForecast(
            @ToolParam(description = "城市名称") String city,
            @ToolParam(description = "预报天数，1~7填") int days) {

        StringBuilder sb = new StringBuilder();
        sb.append(city).append(" 未来 ").append(days).append(" 天天气预报：\n");
        String[] weathers = {"晴", "多云", "小雨", "阴", "大风"};
        for (int i = 1; i <= days; i++) {
            sb.append(String.format("第%d天：%s，20-%d°C\n", i, weathers[i % weathers.length], 20 + i));
        }
        return sb.toString();
    }
}