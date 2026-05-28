package com.agony.langchain4jai.controller.tools;

import com.agony.langchain4jai.service.tools.FinanceAssistant;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author: Agony
 * @create: 2026/5/28 12:39
 * @describe:
 */
@RestController
@RequestMapping("/tool/finance")
public class FinanceController {

    private final FinanceAssistant financeAssistant;

    private final ApplicationContext applicationContext;

    public FinanceController(@Qualifier("financeAssistantWithTools") FinanceAssistant financeAssistant,
                             ApplicationContext applicationContext) {
        this.financeAssistant = financeAssistant;
        this.applicationContext = applicationContext;
    }

    @GetMapping
    public String chat(@RequestParam String message) {
        return financeAssistant.chat(message);
    }

    @PostConstruct
    public void printInjectedBean() {
        String[] names = applicationContext.getBeanNamesForType(FinanceAssistant.class);
        System.out.println("FinanceAssistant beans: " + Arrays.toString(names));

        for (String name : names) {
            Object bean = applicationContext.getBean(name);
            System.out.println(name + " -> same instance: " + (bean == financeAssistant));
        }
    }
}