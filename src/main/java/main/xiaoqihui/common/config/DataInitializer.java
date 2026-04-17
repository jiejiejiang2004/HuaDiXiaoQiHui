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
        initPermissionsAndRoles();
        initMessageTemplates();
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

    private void initPermissionsAndRoles() {
        Integer permissionCount = jdbcTemplate.queryForObject("select count(1) from sys_permission", Integer.class);
        if (permissionCount == null || permissionCount == 0) {
            jdbcTemplate.update(
                "insert into sys_permission (permission_name, permission_code, menu_key, description) values (?, ?, ?, ?)",
                "用户管理",
                "admin:user:manage",
                "candidate",
                "查看并维护求职者和企业账号"
            );
            jdbcTemplate.update(
                "insert into sys_permission (permission_name, permission_code, menu_key, description) values (?, ?, ?, ?)",
                "审核管理",
                "admin:audit:manage",
                "job-audit",
                "审核企业认证、职位和公告"
            );
            jdbcTemplate.update(
                "insert into sys_permission (permission_name, permission_code, menu_key, description) values (?, ?, ?, ?)",
                "系统配置",
                "admin:system:manage",
                "banner",
                "维护轮播图、分类、消息模板与角色"
            );
            jdbcTemplate.update(
                "insert into sys_permission (permission_name, permission_code, menu_key, description) values (?, ?, ?, ?)",
                "统计分析",
                "admin:statistics:view",
                "statistics",
                "查看平台统计与导出报表"
            );
        }

        Integer roleCount = jdbcTemplate.queryForObject("select count(1) from sys_role", Integer.class);
        if (roleCount == null || roleCount == 0) {
            jdbcTemplate.update(
                "insert into sys_role (role_name, role_code, remark, status) values (?, ?, ?, ?)",
                "平台管理员",
                "PLATFORM_ADMIN",
                "默认全量后台管理角色",
                "ACTIVE"
            );
        }

        Integer relationCount = jdbcTemplate.queryForObject("select count(1) from sys_role_permission", Integer.class);
        if (relationCount == null || relationCount == 0) {
            Long roleId = jdbcTemplate.queryForObject(
                "select role_id from sys_role where role_code = ? limit 1",
                Long.class,
                "PLATFORM_ADMIN"
            );
            if (roleId != null) {
                jdbcTemplate.update(
                    """
                    insert ignore into sys_role_permission (role_id, permission_id)
                    select ?, permission_id from sys_permission
                    """,
                    roleId
                );
            }
        }
    }

    private void initMessageTemplates() {
        Integer count = jdbcTemplate.queryForObject("select count(1) from sys_message_template", Integer.class);
        if (count != null && count > 0) {
            return;
        }
        jdbcTemplate.update(
            """
            insert into sys_message_template (type, title_template, content_template, channels, enabled)
            values (?, ?, ?, ?, ?)
            """,
            "AUDIT_RESULT",
            "审核结果通知",
            "您好，{targetName} 的审核结果为 {result}，请及时登录平台查看详情。",
            "INSITE,EMAIL",
            "ACTIVE"
        );
        jdbcTemplate.update(
            """
            insert into sys_message_template (type, title_template, content_template, channels, enabled)
            values (?, ?, ?, ?, ?)
            """,
            "INTERVIEW",
            "面试邀约通知",
            "您收到新的面试邀约，时间为 {interviewTime}，请尽快确认。",
            "INSITE,EMAIL",
            "ACTIVE"
        );
        jdbcTemplate.update(
            """
            insert into sys_message_template (type, title_template, content_template, channels, enabled)
            values (?, ?, ?, ?, ?)
            """,
            "LOGIN_CODE",
            "邮箱验证码",
            "您的登录验证码为 {code}，5 分钟内有效，请勿泄露给他人。",
            "EMAIL",
            "ACTIVE"
        );
    }
}
