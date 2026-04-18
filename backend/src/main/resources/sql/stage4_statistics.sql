USE xiaoqihui;

-- ==========================================
-- 1. 安全添加字段 (使用存储过程避免语法错误)
-- ==========================================

DELIMITER $$

CREATE PROCEDURE UpdateJobPositionColumns()
BEGIN
    -- 检查 job_position 表是否缺少 view_count 字段
    IF NOT EXISTS (
        SELECT 1 
        FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = 'xiaoqihui' 
        AND TABLE_NAME = 'job_position' 
        AND COLUMN_NAME = 'view_count'
    ) THEN
        -- 如果不存在，则添加该字段
        ALTER TABLE job_position 
        ADD COLUMN view_count INT NOT NULL DEFAULT 0 AFTER contact_mobile;
    END IF;
END$$

DELIMITER ;

-- 执行存储过程
CALL UpdateJobPositionColumns();

-- 删除存储过程
DROP PROCEDURE IF EXISTS UpdateJobPositionColumns;

-- ==========================================
-- 2. 创建表 (保持原有逻辑，IF NOT EXISTS 是正确的)
-- ==========================================

CREATE TABLE IF NOT EXISTS sys_export_task (
  task_id VARCHAR(64) PRIMARY KEY,
  task_type VARCHAR(30) NOT NULL,
  user_id BIGINT NOT NULL,
  file_id VARCHAR(64) NULL,
  download_url VARCHAR(255) NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'SUCCESS',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_export_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
);

-- ==========================================
-- 3. 创建索引
-- ==========================================

CREATE INDEX idx_export_user_type ON sys_export_task(user_id, task_type);