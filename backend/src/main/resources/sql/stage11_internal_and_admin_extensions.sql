USE xiaoqihui;

CREATE TABLE IF NOT EXISTS sys_operation_log (
  log_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  user_name VARCHAR(50) NOT NULL,
  action VARCHAR(50) NOT NULL,
  resource VARCHAR(100) NOT NULL,
  detail TEXT NULL,
  ip VARCHAR(64) NULL,
  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_operation_user FOREIGN KEY (user_id) REFERENCES sys_user(user_id)
);

CREATE INDEX idx_operation_user_time ON sys_operation_log(user_id, create_time);
CREATE INDEX idx_operation_action_time ON sys_operation_log(action, create_time);
