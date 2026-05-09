-- 创建数据库（PostgreSQL 不支持 IF NOT EXISTS）
CREATE DATABASE jc_ai;

-- 切换数据库（psql 中执行）
\c dbtest;

-- =========================
-- 订单表
-- =========================
CREATE TABLE order_info
(
    id                 VARCHAR(32) PRIMARY KEY,
    user_id            BIGINT         NOT NULL,
    total_amount       NUMERIC(10, 2) NOT NULL,
    status             VARCHAR(20)    NOT NULL,
    tracking_number    VARCHAR(64),
    estimated_delivery DATE,
    created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_user_id ON order_info (user_id);

-- =========================
-- 商品表
-- =========================
CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(128)   NOT NULL,
    price       NUMERIC(10, 2) NOT NULL,
    stock       INTEGER        NOT NULL DEFAULT 0,
    rating      NUMERIC(2, 1)  NOT NULL DEFAULT 5.0,
    description VARCHAR(512),
    created_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- PostgreSQL 全文搜索
CREATE INDEX ft_name_desc
    ON product
        USING GIN (
                   TO_TSVECTOR('simple',
                               COALESCE(name, '') || ' ' || COALESCE(description, '')
                   )
            );

-- =========================
-- 测试数据
-- =========================
INSERT INTO order_info (id,
                        user_id,
                        total_amount,
                        status,
                        tracking_number,
                        estimated_delivery,
                        created_at,
                        updated_at)
VALUES ('ORD001', 1001, 599.00, 'SHIPPED', 'SF1234567890', '2025-03-10', NOW(), NOW()),
       ('ORD002', 1001, 1299.00, 'PENDING', NULL, NULL, NOW(), NOW()),
       ('ORD003', 1002, 299.00, 'DELIVERED', 'YT9876543210', '2025-03-05', NOW(), NOW());

INSERT INTO product (id,
                     name,
                     price,
                     stock,
                     rating,
                     description,
                     created_at)
VALUES (1, 'iPhone 16 Pro 256G', 8999.00, 23, 4.8, '苹果最新旗舰，A18 Pro芯片', NOW()),
       (2, 'iPhone 16 Pro 512G', 9999.00, 5, 4.8, '苹果最新旗舰，大存储版本', NOW()),
       (3, '华为 Mate 70 Pro', 6999.00, 12, 4.7, '华为旗舰，麒麟芯片', NOW()),
       (4, 'AirPods Pro 2', 1799.00, 50, 4.6, '苹果降噪耳机', NOW());