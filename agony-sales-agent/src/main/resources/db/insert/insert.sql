-- 大区
INSERT INTO sa_sales_region (id, name)
VALUES (1, '华东区'),
       (2, '华南区'),
       (3, '华北区'),
       (4, '西南区');


-- 销售员（每区 3 人，含 1 名主管）
INSERT INTO sa_sales_rep (id, name, region_id, role, email)
VALUES
    -- 华东区
    (1, '李明', 1, 'SALES_MANAGER', 'liming@jichi.com'),
    (2, '张伟', 1, 'SALES_REP', 'zhangwei@jichi.com'),
    (3, '王芳', 1, 'SALES_REP', 'wangfang@jichi.com'),
    -- 华南区
    (4, '陈强', 2, 'SALES_MANAGER', 'chenqiang@jichi.com'),
    (5, '刘洋', 2, 'SALES_REP', 'liuyang@jichi.com'),
    (6, '赵雪', 2, 'SALES_REP', 'zhaoxue@jichi.com'),
    -- 华北区
    (7, '孙磊', 3, 'SALES_MANAGER', 'sunlei@jichi.com'),
    (8, '张磊', 3, 'SALES_REP', 'zhanglei@jichi.com'),
    (9, '周丽', 3, 'SALES_REP', 'zhouli@jichi.com'),
    -- 西南区
    (10, '吴刚', 4, 'SALES_MANAGER', 'wugang@jichi.com'),
    (11, '郑华', 4, 'SALES_REP', 'zhenghua@jichi.com'),
    (12, '林敏', 4, 'SALES_REP', 'linmin@jichi.com'),
    -- 总监（全国）
    (13, '黄总', 1, 'SALES_DIRECTOR', 'huang@jichi.com');


-- 产品（4 个品类，20 个 SKU）
INSERT INTO sa_product (id, sku_code, name, category, unit_price, cost, status)
VALUES
    -- 数码产品（高客单价）
    (1, 'SKU-1001', '华为 Mate 60 Pro 手机', '数码产品', 6999.00, 4200.00, 'ACTIVE'),
    (2, 'SKU-1002', '苹果 iPhone 15 手机', '数码产品', 7999.00, 5100.00, 'ACTIVE'),
    (3, 'SKU-1003', '联想 ThinkPad X1 笔记本', '数码产品', 9999.00, 6800.00, 'ACTIVE'),
    (4, 'SKU-1004', '索尼 WH-1000XM5 耳机', '数码产品', 2299.00, 1100.00, 'ACTIVE'),
    (5, 'SKU-1005', '小米 14 Ultra 手机', '数码产品', 5999.00, 3600.00, 'ACTIVE'),
    -- 数码（含异常SKU）
    (6, 'SKU-8821', '智能手表 Pro', '数码产品', 1299.00, 650.00, 'ACTIVE'),
    -- 家用电器（中高客单价）
    (7, 'SKU-2001', '戴森 V15 吸尘器', '家用电器', 4990.00, 2800.00, 'ACTIVE'),
    (8, 'SKU-2002', '西门子洗碗机', '家用电器', 5999.00, 3500.00, 'ACTIVE'),
    (9, 'SKU-2003', '美的空调 1.5P', '家用电器', 3299.00, 1900.00, 'ACTIVE'),
    (10, 'SKU-2004', '苏泊尔电饭煲', '家用电器', 599.00, 280.00, 'ACTIVE'),
    (11, 'SKU-2005', '飞利浦空气净化器', '家用电器', 2199.00, 1200.00, 'ACTIVE'),
    -- 服装配饰（低客单价、量大）
    (12, 'SKU-3001', '耐克 Air Max 运动鞋', '服装配饰', 899.00, 420.00, 'ACTIVE'),
    (13, 'SKU-3002', '优衣库羊绒大衣', '服装配饰', 799.00, 350.00, 'ACTIVE'),
    (14, 'SKU-3003', '阿迪达斯运动套装', '服装配饰', 699.00, 310.00, 'ACTIVE'),
    (15, 'SKU-3004', '蔻驰女包', '服装配饰', 2599.00, 1100.00, 'ACTIVE'),
    -- 其他
    (16, 'SKU-4001', '得力文具套装', '其他', 99.00, 40.00, 'ACTIVE'),
    (17, 'SKU-4002', '金融理财书籍套装', '其他', 299.00, 120.00, 'ACTIVE'),
    (18, 'SKU-4003', '瑜伽垫专业版', '其他', 399.00, 160.00, 'ACTIVE'),
    (19, 'SKU-4004', '咖啡机胶囊套装', '其他', 699.00, 280.00, 'ACTIVE'),
    (20, 'SKU-4005', '护肤品礼盒', '其他', 899.00, 350.00, 'ACTIVE');