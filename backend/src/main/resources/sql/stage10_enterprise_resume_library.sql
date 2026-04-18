USE xiaoqihui;

CREATE TABLE IF NOT EXISTS enterprise_resume_favorite (
  favorite_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  enterprise_id BIGINT NOT NULL,
  resume_id BIGINT NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_enterprise_resume_favorite (enterprise_id, resume_id),
  KEY idx_enterprise_resume_favorite_enterprise (enterprise_id),
  KEY idx_enterprise_resume_favorite_resume (resume_id)
);
