USE xiaoqihui;

CREATE TABLE IF NOT EXISTS resume_attachment (
  attachment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  resume_id BIGINT NOT NULL,
  file_id VARCHAR(64) NOT NULL,
  file_name VARCHAR(255) NOT NULL,
  file_url VARCHAR(255) NOT NULL,
  uploader_id BIGINT NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_resume_attachment_resume FOREIGN KEY (resume_id) REFERENCES resume(resume_id) ON DELETE CASCADE,
  CONSTRAINT fk_resume_attachment_file FOREIGN KEY (file_id) REFERENCES sys_file_record(file_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS enterprise_job_refresh_log (
  log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  enterprise_id BIGINT NOT NULL,
  job_id BIGINT NOT NULL,
  refresh_date DATE NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_job_refresh_date (job_id, refresh_date),
  KEY idx_enterprise_refresh_date (enterprise_id, refresh_date)
);
