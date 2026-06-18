CREATE TABLE IF NOT EXISTS sa_sales_rep
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    region_id  BIGINT      NOT NULL,
    role       VARCHAR(20) NOT NULL DEFAULT 'SALES_REP',
    email      VARCHAR(100)         DEFAULT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 索引
CREATE INDEX idx_region
    ON sa_sales_rep (region_id);

-- 表注释
COMMENT ON TABLE sa_sales_rep IS '销售员';

-- 字段注释
COMMENT ON COLUMN sa_sales_rep.id IS '销售员ID';
COMMENT ON COLUMN sa_sales_rep.name IS '姓名';
COMMENT ON COLUMN sa_sales_rep.region_id IS '所属大区';
COMMENT ON COLUMN sa_sales_rep.role IS '角色：SALES_REP/SALES_MANAGER/SALES_DIRECTOR';
COMMENT ON COLUMN sa_sales_rep.email IS '邮箱';
COMMENT ON COLUMN sa_sales_rep.created_at IS '创建时间';