USE xiaoqihui;

CREATE TABLE IF NOT EXISTS job_collection (
  collection_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  job_id BIGINT NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_job_collection_user_job (user_id, job_id),
  KEY idx_job_collection_user (user_id),
  KEY idx_job_collection_job (job_id)
);

CREATE TABLE IF NOT EXISTS enterprise_interview (
  interview_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  apply_id BIGINT NOT NULL,
  enterprise_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  resume_id BIGINT NOT NULL,
  job_id BIGINT NOT NULL,
  interview_time DATETIME NOT NULL,
  interview_type VARCHAR(20) NOT NULL,
  interview_place VARCHAR(255) NULL,
  interview_link VARCHAR(255) NULL,
  contact_name VARCHAR(50) NOT NULL,
  contact_mobile VARCHAR(30) NOT NULL,
  remark VARCHAR(500) NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY idx_enterprise_interview_enterprise (enterprise_id),
  KEY idx_enterprise_interview_user (user_id),
  KEY idx_enterprise_interview_apply (apply_id)
);
