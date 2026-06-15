package com.agony.agent.model;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * @author: Agony
 * @create: 2026/6/15 13:52
 * @describe:
 */
@Getter
public class Order {

    private String id;
    private String userId;
    private double actualAmount;
    private String status;          // PAID / SHIPPED / COMPLETED / REFUNDED
    private LocalDateTime createdAt;
    private LocalDateTime signedAt; // 签收时间，null 表示尚未签收

    public Order(String id, String userId, double actualAmount,
                 String status, LocalDateTime createdAt, LocalDateTime signedAt) {
        this.id = id;
        this.userId = userId;
        this.actualAmount = actualAmount;
        this.status = status;
        this.createdAt = createdAt;
        this.signedAt = signedAt;
    }
}