package com.agony.agent.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author: Agony
 * @create: 2026/6/15 14:10
 * @describe:
 */
@Component
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public void sendRefundNotification(String userId, String orderId, double amount) {
        log.info("[通知] 用户 {} 订单 {} 退款 ¥{} 成功，短信/邮件已发送",
                userId, orderId, String.format("%.2f", amount));
    }

    public void notifyApprover(String approverEmail, String message) {
        log.info("[审批通知] 发送给 {}：{}", approverEmail, message);
    }
}