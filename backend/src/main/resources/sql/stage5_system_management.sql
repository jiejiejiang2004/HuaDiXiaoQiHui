USE xiaoqihui;

CREATE TABLE IF NOT EXISTS sys_banner (
  banner_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(100) NOT NULL,
  image_file_id VARCHAR(64) NOT NULL,
  image_url VARCHAR(255) NOT NULL,
  link_url VARCHAR(255) NULL,
  sort INT NOT NULL DEFAULT 0,
  status VARCHAR(20) NOT NULL DEFAULT 'ONLINE',
  start_time DATETIME NULL,
  end_time DATETIME NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS sys_role (
  role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  role_name VARCHAR(50) NOT NULL,
  role_code VARCHAR(50) NOT NULL,
  remark VARCHAR(255) NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_role_name (role_name),
  UNIQUE KEY uk_role_code (role_code)
);

CREATE TABLE IF NOT EXISTS sys_permission (
  permission_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  permission_name VARCHAR(100) NOT NULL,
  permission_code VARCHAR(100) NOT NULL,
  menu_key VARCHAR(100) NULL,
  description VARCHAR(255) NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_permission_code (permission_code)
);

CREATE TABLE IF NOT EXISTS sys_role_permission (
  role_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (role_id, permission_id),
  CONSTRAINT fk_sys_role_permission_role FOREIGN KEY (role_id) REFERENCES sys_role(role_id) ON DELETE CASCADE,
  CONSTRAINT fk_sys_role_permission_permission FOREIGN KEY (permission_id) REFERENCES sys_permission(permission_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS sys_message_template (
  template_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  type VARCHAR(50) NOT NULL,
  title_template VARCHAR(200) NOT NULL,
  content_template TEXT NOT NULL,
  channels VARCHAR(100) NOT NULL DEFAULT 'INSITE,EMAIL',
  enabled VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_template_type (type)
);

CREATE INDEX idx_sys_banner_status_sort ON sys_banner(status, sort);
CREATE INDEX idx_sys_role_status ON sys_role(status);
CREATE INDEX idx_sys_permission_menu ON sys_permission(menu_key);
