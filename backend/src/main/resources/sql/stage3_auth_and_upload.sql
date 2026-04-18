USE xiaoqihui;

CREATE TABLE IF NOT EXISTS company_auth (
  auth_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  company_id BIGINT NOT NULL,
  credit_code VARCHAR(50) NOT NULL,
  legal_person VARCHAR(50) NOT NULL,
  license_file_id VARCHAR(64) NOT NULL,
  license_image VARCHAR(255) NOT NULL,
  logo_file_id VARCHAR(64) NULL,
  logo_url VARCHAR(255) NULL,
  apply_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  audit_time DATETIME NULL,
  audit_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
  audit_remark VARCHAR(500) NULL,
  auditor_id BIGINT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_company_auth_company FOREIGN KEY (company_id) REFERENCES company_info(enterprise_id)
);

CREATE TABLE IF NOT EXISTS sys_file_record (
  file_id VARCHAR(64) PRIMARY KEY,
  biz_type VARCHAR(30) NOT NULL,
  original_name VARCHAR(255) NOT NULL,
  file_url VARCHAR(255) NOT NULL,
  file_path VARCHAR(255) NOT NULL,
  file_size BIGINT NOT NULL,
  file_type VARCHAR(20) NOT NULL,
  uploader_id BIGINT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_file_uploader FOREIGN KEY (uploader_id) REFERENCES sys_user(user_id)
);

CREATE INDEX idx_company_auth_company_status ON company_auth(company_id, audit_status);
CREATE INDEX idx_file_biz_type ON sys_file_record(biz_type);
