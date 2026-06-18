CREATE TABLE IF NOT EXISTS sa_product
(
    id         BIGSERIAL PRIMARY KEY,
    sku_code   VARCHAR(50)    NOT NULL,
    name       VARCHAR(200)   NOT NULL,
    category   VARCHAR(50)    NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    cost       DECIMAL(10, 2) NOT NULL,
    status     VARCHAR(20)    NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT uk_sku UNIQUE (sku_code)
);

-- 索引
CREATE INDEX idx_category
    ON sa_product (category);

-- 表注释
COMMENT ON TABLE sa_product IS '产品';

-- 字段注释
COMMENT ON COLUMN sa_product.id IS '产品ID';
COMMENT ON COLUMN sa_product.sku_code IS 'SKU编码';
COMMENT ON COLUMN sa_product.name IS '产品名称';
COMMENT ON COLUMN sa_product.category IS '品类：数码产品/家用电器/服装配饰/其他';
COMMENT ON COLUMN sa_product.unit_price IS '售价';
COMMENT ON COLUMN sa_product.cost IS '成本';
COMMENT ON COLUMN sa_product.status IS '状态：ACTIVE/INACTIVE';
COMMENT ON COLUMN sa_product.created_at IS '创建时间';