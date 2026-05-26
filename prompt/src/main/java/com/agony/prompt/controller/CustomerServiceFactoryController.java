package com.agony.prompt.controller;

import com.agony.prompt.entity.CustomerServiceConfig;
import com.agony.prompt.service.CustomerServiceFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Agony
 * @create: 2026/5/26 11:05
 * @describe:
 */
@RestController
@RequestMapping("/customer-service")
public class CustomerServiceFactoryController {

    private final CustomerServiceFactory customerServiceFactory;

    public CustomerServiceFactoryController(CustomerServiceFactory customerServiceFactory) {
        this.customerServiceFactory = customerServiceFactory;
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String message) {
        CustomerServiceConfig config = new CustomerServiceConfig(
                "鸡翅商城",
                "小鸡",
                List.of("商品咨询", "订单查询", "售后服务"),
                List.of("退款纠纷", "法律问题"),
                "专业友好"
        );

        ChatClient client = customerServiceFactory.createForTenant(config);
        return client.prompt().user(message).call().content();
    }
}