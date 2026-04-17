package main.xiaoqihui.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initAdminUser();
        initCategories();
        initNotices();
    }

    private void initAdminUser() {
        Integer count = jdbcTemplate.queryForObject(
            "select count(1) from sys_user where mobile = ?",
            Integer.class,
            "18800000000"
        );
        if (count != null && count > 0) {
            return;
        }
        jdbcTemplate.update(
            """
            insert into sys_user (mobile, password, real_name, identity_type, user_type, status)
            values (?, ?, ?, ?, ?, ?)
            """,
            "18800000000",
            passwordEncoder.encode("Admin@123456"),
            "平台管理员",
            "ADMIN",
            "ADMIN",
            "ACTIVE"
        );
    }

    private void initCategories() {
        Integer count = jdbcTemplate.queryForObject("select count(1) from job_category", Integer.class);
        if (count != null && count > 0) {
            return;
        }
        jdbcTemplate.update(
            "insert into job_category (parent_id, name, code, sort) values (0, '互联网技术', 'IT', 1)"
        );
        jdbcTemplate.update(
            "insert into job_category (parent_id, name, code, sort) values (0, '智能制造', 'MANUFACTURE', 2)"
        );
        jdbcTemplate.update(
            "insert into job_category (parent_id, name, code, sort) values (1, '后端开发', 'JAVA_BACKEND', 1)"
        );
    }

    private void initNotices() {
        Integer count = jdbcTemplate.queryForObject("select count(1) from job_notice", Integer.class);
        if (count != null && count > 0) {
            return;
        }
        Long adminId = jdbcTemplate.queryForObject(
            "select user_id from sys_user where mobile = ?",
            Long.class,
            "18800000000"
        );
        jdbcTemplate.update(
            """
            insert into job_notice (title, content, type, status, publish_time, create_by)
            values (?, ?, ?, ?, now(), ?)
            """,
            "平台招聘周启动",
            "请各企业及时完善招聘信息，管理员将统一开展内容审核。",
            "JOB_FAIR",
            "ONLINE",
            adminId
        );
    }
}
