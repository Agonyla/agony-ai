package com.agony.langchain4jai.controller.tools;

import com.agony.langchain4jai.service.tools.WeatherAssistant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Agony
 * @create: 2026/5/28 11:13
 * @describe:
 */
@RestController
@RequestMapping("/tool/weather")
public class WeatherController {

    private final WeatherAssistant assistant;

    public WeatherController(@Qualifier("weatherAssistantWithTools") WeatherAssistant assistant) {
        this.assistant = assistant;
    }

    @GetMapping
    public String chat(@RequestParam String message,
                       @RequestHeader(value = "X-Session-Id", defaultValue = "default") String sessionId) {
        return assistant.chat(sessionId, message);
    }
}