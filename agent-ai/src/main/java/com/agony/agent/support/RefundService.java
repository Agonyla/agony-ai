package com.agony.agent.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author: Agony
 * @create: 2026/6/15 14:09
 * @describe:
 */
@Component
public class RefundService {

    private static final Logger log = LoggerFactory.getLogger(RefundService.class);

    public void process(String orderId, double amount) {
        // 实际项目接支付系统 API；这里打日志模拟
        log.info("[退款执行] 订单 {} 退款 ¥{}", orderId, String.format("%.2f", amount));
    }
}