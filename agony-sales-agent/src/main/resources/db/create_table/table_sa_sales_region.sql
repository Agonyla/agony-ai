CREATE TABLE IF NOT EXISTS sa_sales_region
(
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(50) NOT NULL,
    parent_region_id BIGINT               DEFAULT NULL,
    created_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 唯一约束
ALTER TABLE sa_sales_region
    ADD CONSTRAINT uk_name UNIQUE (name);

-- 表注释
COMMENT ON TABLE sa_sales_region IS '销售大区';

-- 字段注释
COMMENT ON COLUMN sa_sales_region.id IS '大区ID';
COMMENT ON COLUMN sa_sales_region.name IS '大区名称，如：华东区';
COMMENT ON COLUMN sa_sales_region.parent_region_id IS '上级大区，NULL表示顶级';
COMMENT ON COLUMN sa_sales_region.created_at IS '创建时间';