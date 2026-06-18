CREATE TABLE IF NOT EXISTS sa_sales_order
(
    id            BIGSERIAL PRIMARY KEY,
    order_no      VARCHAR(50)    NOT NULL,
    rep_id        BIGINT         NOT NULL,
    product_id    BIGINT         NOT NULL,
    region_id     BIGINT         NOT NULL,
    customer_name VARCHAR(100)   NOT NULL,
    quantity      INT            NOT NULL,
    unit_price    DECIMAL(10, 2) NOT NULL,
    amount        DECIMAL(12, 2) NOT NULL,
    cost          DECIMAL(12, 2) NOT NULL,
    profit        DECIMAL(12, 2) NOT NULL,
    status        VARCHAR(20)    NOT NULL DEFAULT 'COMPLETED',
    order_date    DATE           NOT NULL,
    created_at    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_order_no UNIQUE (order_no)
);

-- 索引
CREATE INDEX idx_rep
    ON sa_sales_order (rep_id);

CREATE INDEX idx_product
    ON sa_sales_order (product_id);

CREATE INDEX idx_region
    ON sa_sales_order (region_id);

CREATE INDEX idx_order_date
    ON sa_sales_order (order_date);

CREATE INDEX idx_status
    ON sa_sales_order (status);

-- 表注释
COMMENT ON TABLE sa_sales_order IS '销售订单';

-- 字段注释
COMMENT ON COLUMN sa_sales_order.id IS '订单ID';
COMMENT ON COLUMN sa_sales_order.order_no IS '订单号';
COMMENT ON COLUMN sa_sales_order.rep_id IS '销售员ID';
COMMENT ON COLUMN sa_sales_order.product_id IS '产品ID';
COMMENT ON COLUMN sa_sales_order.region_id IS '销售大区ID';
COMMENT ON COLUMN sa_sales_order.customer_name IS '客户名称';
COMMENT ON COLUMN sa_sales_order.quantity IS '销售数量';
COMMENT ON COLUMN sa_sales_order.unit_price IS '成交单价';
COMMENT ON COLUMN sa_sales_order.amount IS '成交金额（quantity * unit_price）';
COMMENT ON COLUMN sa_sales_order.cost IS '成本总额';
COMMENT ON COLUMN sa_sales_order.profit IS '毛利（amount - cost）';
COMMENT ON COLUMN sa_sales_order.status IS '状态：COMPLETED/REFUNDED/CANCELLED';
COMMENT ON COLUMN sa_sales_order.order_date IS '下单日期';
COMMENT ON COLUMN sa_sales_order.created_at IS '创建时间';