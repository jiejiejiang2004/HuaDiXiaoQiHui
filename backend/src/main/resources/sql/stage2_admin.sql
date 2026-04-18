USE xiaoqihui;

-- ==========================================
-- 1. 修改表结构 (使用存储过程避免字段已存在报错)
-- ==========================================

DELIMITER $$

-- 创建存储过程：添加字段
CREATE PROCEDURE UpdateTableColumns()
BEGIN
    -- 1. 给 company_info 表添加 auth_remark
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = 'xiaoqihui' 
        AND TABLE_NAME = 'company_info' 
        AND COLUMN_NAME = 'auth_remark'
    ) THEN
        ALTER TABLE company_info 
        ADD COLUMN auth_remark VARCHAR(500) NULL AFTER auth_status;
    END IF;

    -- 2. 给 job_position 表添加 audit_remark
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = 'xiaoqihui' 
        AND TABLE_NAME = 'job_position' 
        AND COLUMN_NAME = 'audit_remark'
    ) THEN
        ALTER TABLE job_position 
        ADD COLUMN audit_remark VARCHAR(500) NULL AFTER status;
    END IF;
END$$

DELIMITER ;

-- 执行存储过程
CALL UpdateTableColumns();

-- 删除存储过程
DROP PROCEDURE IF EXISTS UpdateTableColumns;

-- ==========================================
-- 2. 创建表 (这部分语法是正确的)
-- ==========================================

CREATE TABLE IF NOT EXISTS job_category (
  category_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  parent_id BIGINT NOT NULL DEFAULT 0,
  name VARCHAR(50) NOT NULL,
  code VARCHAR(50) NULL,
  sort INT NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS job_notice (
  notice_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  type VARCHAR(30) NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  audit_remark VARCHAR(500) NULL,
  publish_time DATETIME NULL,
  create_by BIGINT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_notice_creator FOREIGN KEY (create_by) REFERENCES sys_user(user_id)
);

CREATE TABLE IF NOT EXISTS sys_audit_log (
  audit_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  biz_type VARCHAR(20) NOT NULL,
  biz_id BIGINT NOT NULL,
  before_status VARCHAR(20) NULL,
  after_status VARCHAR(20) NOT NULL,
  audit_result VARCHAR(20) NOT NULL,
  audit_remark VARCHAR(500) NULL,
  auditor_id BIGINT NOT NULL,
  auditor_name VARCHAR(50) NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_audit_user FOREIGN KEY (auditor_id) REFERENCES sys_user(user_id)
);

-- ==========================================
-- 3. 创建索引
-- ==========================================

CREATE INDEX idx_category_parent ON job_category(parent_id);
CREATE INDEX idx_notice_status_type ON job_notice(status, type);
CREATE INDEX idx_audit_biz ON sys_audit_log(biz_type, biz_id);