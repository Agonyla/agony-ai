package com.agony.langchain4jai.controller.tools;

import com.agony.langchain4jai.service.tools.OrderAssistant;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Agony
 * @create: 2026/5/28 11:23
 * @describe:
 */
@RestController
@RequestMapping("/tool/order")
public class OrderAssistantController {

    private final OrderAssistant orderAssistant;

    private final ApplicationContext applicationContext;

    public OrderAssistantController(@Qualifier("orderAssistantWithTools") OrderAssistant orderAssistant,
                                    ApplicationContext applicationContext) {

        this.orderAssistant = orderAssistant;
        this.applicationContext = applicationContext;
    }

    @GetMapping
    public String chat(@RequestParam String message,
                       @RequestHeader(value = "X-Session-Id", defaultValue = "default") String sessionId) {
        return orderAssistant.chat(sessionId, message);
    }

    // @PostConstruct
    // public void printInjectedBean() {
    //     String[] names = applicationContext.getBeanNamesForType(OrderAssistant.class);
    //     System.out.println("OrderAssistant beans: " + Arrays.toString(names));
    //
    //     for (String name : names) {
    //         Object bean = applicationContext.getBean(name);
    //         System.out.println(name + " -> same instance: " + (bean == orderAssistant));
    //     }
    // }
}