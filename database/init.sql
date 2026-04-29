-- ============================================
-- 逐风马马拉松报名系统数据库初始化脚本
-- 数据库名: zhufengma
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS zhufengma DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE zhufengma;

-- ============================================
-- 1. 用户表
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    id_card VARCHAR(18) NOT NULL UNIQUE COMMENT '身份证号',
    phone VARCHAR(11) NOT NULL COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    gender ENUM('male', 'female') NOT NULL COMMENT '性别',
    birth_date DATE NOT NULL COMMENT '出生日期',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(11) COMMENT '紧急联系电话',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0:否 1:是',
    INDEX idx_username (username),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ============================================
-- 2. 管理员表
-- ============================================
CREATE TABLE IF NOT EXISTS admins (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码（加密存储）',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(11) COMMENT '手机号',
    role ENUM('super_admin', 'admin') DEFAULT 'admin' COMMENT '角色',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0:否 1:是'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- ============================================
-- 3. 赛事表
-- ============================================
CREATE TABLE IF NOT EXISTS events (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '赛事ID',
    name VARCHAR(100) NOT NULL COMMENT '赛事名称',
    description TEXT COMMENT '赛事描述',
    event_date DATE NOT NULL COMMENT '赛事日期',
    event_time TIME COMMENT '赛事时间',
    location VARCHAR(200) NOT NULL COMMENT '赛事地点',
    registration_start_date DATE NOT NULL COMMENT '报名开始日期',
    registration_end_date DATE NOT NULL COMMENT '报名截止日期',
    status ENUM('draft', 'published', 'offline', 'ended') DEFAULT 'draft' COMMENT '状态',
    created_by BIGINT COMMENT '创建人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0:否 1:是',
    INDEX idx_event_date (event_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='赛事表';

-- ============================================
-- 4. 赛事组别表
-- ============================================
CREATE TABLE IF NOT EXISTS event_groups (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '组别ID',
    event_id BIGINT NOT NULL COMMENT '赛事ID',
    name VARCHAR(50) NOT NULL COMMENT '组别名称',
    distance VARCHAR(20) NOT NULL COMMENT '距离',
    max_participants INT NOT NULL COMMENT '最大参赛人数',
    registration_fee DECIMAL(10, 2) NOT NULL COMMENT '报名费',
    age_min INT COMMENT '最小年龄限制',
    age_max INT COMMENT '最大年龄限制',
    gender_limit ENUM('all', 'male', 'female') DEFAULT 'all' COMMENT '性别限制',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0:否 1:是',
    INDEX idx_event_id (event_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='赛事组别表';

-- ============================================
-- 5. 报名表
-- ============================================
CREATE TABLE IF NOT EXISTS registrations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报名ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    event_id BIGINT NOT NULL COMMENT '赛事ID',
    group_id BIGINT NOT NULL COMMENT '组别ID',
    status ENUM('pending', 'approved', 'rejected', 'cancelled') DEFAULT 'pending' COMMENT '报名状态',
    shirt_size ENUM('XS', 'S', 'M', 'L', 'XL', 'XXL', 'XXXL') COMMENT 'T恤尺码',
    emergency_contact VARCHAR(50) COMMENT '紧急联系人',
    emergency_phone VARCHAR(11) COMMENT '紧急联系电话',
    medical_history TEXT COMMENT '病史信息',
    remark TEXT COMMENT '备注',
    review_remark TEXT COMMENT '审核备注',
    reviewed_by BIGINT COMMENT '审核人ID',
    reviewed_at TIMESTAMP NULL COMMENT '审核时间',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0:否 1:是',
    INDEX idx_user_id (user_id),
    INDEX idx_event_id (event_id),
    INDEX idx_group_id (group_id),
    INDEX idx_status (status),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES event_groups(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报名表';

-- ============================================
-- 6. 公告表
-- ============================================
CREATE TABLE IF NOT EXISTS announcements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公告ID',
    title VARCHAR(200) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    event_id BIGINT COMMENT '关联赛事ID（可选）',
    type ENUM('general', 'event') DEFAULT 'general' COMMENT '公告类型',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶 0:否 1:是',
    status ENUM('draft', 'published') DEFAULT 'draft' COMMENT '状态',
    created_by BIGINT COMMENT '创建人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0:否 1:是',
    INDEX idx_event_id (event_id),
    INDEX idx_is_top (is_top),
    INDEX idx_status (status),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ============================================
-- 7. 竞赛规程表
-- ============================================
CREATE TABLE IF NOT EXISTS regulations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规程ID',
    event_id BIGINT NOT NULL COMMENT '赛事ID',
    title VARCHAR(200) NOT NULL COMMENT '规程标题',
    content TEXT NOT NULL COMMENT '规程内容',
    version INT DEFAULT 1 COMMENT '版本号',
    status ENUM('draft', 'published') DEFAULT 'draft' COMMENT '状态',
    created_by BIGINT COMMENT '创建人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0:否 1:是',
    INDEX idx_event_id (event_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='竞赛规程表';

-- ============================================
-- 8. 领物须知表
-- ============================================
CREATE TABLE IF NOT EXISTS pickup_notes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '须知ID',
    event_id BIGINT NOT NULL COMMENT '赛事ID',
    title VARCHAR(200) NOT NULL COMMENT '须知标题',
    content TEXT NOT NULL COMMENT '须知内容',
    pickup_date DATE COMMENT '领物日期',
    pickup_location VARCHAR(200) COMMENT '领物地点',
    status ENUM('draft', 'published') DEFAULT 'draft' COMMENT '状态',
    created_by BIGINT COMMENT '创建人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0:否 1:是',
    INDEX idx_event_id (event_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领物须知表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入默认管理员（密码：admin123，使用BCrypt加密）
INSERT INTO admins (username, password, real_name, phone, role) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '系统管理员', '13800138000', 'super_admin');

-- 插入测试用户（密码：user123，使用BCrypt加密）
INSERT INTO users (username, password, real_name, id_card, phone, email, gender, birth_date, emergency_contact, emergency_phone) VALUES 
('zhangsan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '张三', '110101199001011234', '13900139001', 'zhangsan@example.com', 'male', '1990-01-01', '李四', '13800138001'),
('lisi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E', '李四', '110101199202022345', '13900139002', 'lisi@example.com', 'female', '1992-02-02', '王五', '13800138002');

-- 插入测试赛事
INSERT INTO events (name, description, event_date, event_time, location, registration_start_date, registration_end_date, status, created_by) VALUES 
('2026逐风马北京马拉松', '逐风马北京马拉松是中国最具影响力的马拉松赛事之一，吸引了来自世界各地的跑者参与。', '2026-10-15', '07:30:00', '北京市天安门广场', '2026-07-01', '2026-09-30', 'published', 1),
('2026逐风马上海马拉松', '逐风马上海马拉松是中国南方最具影响力的马拉松赛事，赛道穿越上海标志性建筑。', '2026-11-20', '07:00:00', '上海市外滩', '2026-08-01', '2026-10-31', 'published', 1);

-- 插入测试赛事组别
INSERT INTO event_groups (event_id, name, distance, max_participants, registration_fee, age_min, age_max, gender_limit) VALUES 
(1, '全程马拉松', '42.195公里', 10000, 200.00, 20, 65, 'all'),
(1, '半程马拉松', '21.0975公里', 15000, 150.00, 18, 65, 'all'),
(1, '迷你马拉松', '5公里', 5000, 100.00, 10, 70, 'all'),
(2, '全程马拉松', '42.195公里', 8000, 200.00, 20, 65, 'all'),
(2, '半程马拉松', '21.0975公里', 12000, 150.00, 18, 65, 'all');

-- 插入测试报名
INSERT INTO registrations (user_id, event_id, group_id, status, shirt_size, emergency_contact, emergency_phone, medical_history) VALUES 
(1, 1, 1, 'pending', 'L', '李四', '13800138001', '无'),
(2, 1, 2, 'approved', 'M', '王五', '13800138002', '无');

-- 插入测试公告
INSERT INTO announcements (title, content, event_id, type, is_top, status, created_by) VALUES 
('2026逐风马北京马拉松报名开始', '2026逐风马北京马拉松报名已于7月1日正式开始，欢迎广大跑友踊跃报名！', 1, 'event', 1, 'published', 1),
('逐风马马拉松系统上线公告', '逐风马马拉松报名系统正式上线，为您提供便捷的赛事报名服务。', NULL, 'general', 0, 'published', 1);

-- 插入测试竞赛规程
INSERT INTO regulations (event_id, title, content, version, status, created_by) VALUES 
(1, '2026逐风马北京马拉松竞赛规程', '一、主办单位\n北京市体育局\n\n二、竞赛日期和地点\n2026年10月15日（星期日）上午7:30在北京市天安门广场出发\n\n三、竞赛项目\n（一）马拉松（42.195公里）\n（二）半程马拉松（21.0975公里）\n（三）迷你马拉松（5公里）\n\n四、参赛办法\n（一）年龄要求\n1. 马拉松项目年龄限20周岁以上（2006年10月15日以前出生）\n2. 半程马拉松项目年龄限18周岁以上（2008年10月15日以前出生）\n3. 迷你马拉松项目年龄限10周岁以上（2016年10月15日以前出生）', 1, 'published', 1);

-- 插入测试领物须知
INSERT INTO pickup_notes (event_id, title, content, pickup_date, pickup_location, status, created_by) VALUES 
(1, '2026逐风马北京马拉松领物须知', '一、领物时间\n2026年10月13日-14日 9:00-20:00\n\n二、领物地点\n北京市朝阳区奥林匹克公园中心区\n\n三、领物要求\n1. 参赛选手本人领取：凭本人有效身份证件原件\n2. 代领：凭代领人有效身份证件原件及参赛选手有效身份证件复印件\n\n四、领取物品\n参赛包、参赛T恤、号码布、计时芯片、参赛指南等', '2026-10-13', '北京市朝阳区奥林匹克公园中心区', 'published', 1);
