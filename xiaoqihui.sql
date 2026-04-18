/*
 Navicat Premium Dump SQL

 Source Server         : jiejiejiang
 Source Server Type    : MySQL
 Source Server Version : 80039 (8.0.39)
 Source Host           : localhost:3306
 Source Schema         : xiaoqihui

 Target Server Type    : MySQL
 Target Server Version : 80039 (8.0.39)
 File Encoding         : 65001

 Date: 18/04/2026 14:40:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for company_auth
-- ----------------------------
DROP TABLE IF EXISTS `company_auth`;
CREATE TABLE `company_auth`  (
  `auth_id` bigint NOT NULL AUTO_INCREMENT,
  `company_id` bigint NOT NULL,
  `credit_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `legal_person` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `license_file_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `license_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `logo_file_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `audit_time` datetime NULL DEFAULT NULL,
  `audit_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING',
  `audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `auditor_id` bigint NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`auth_id`) USING BTREE,
  INDEX `idx_company_auth_company_status`(`company_id` ASC, `audit_status` ASC) USING BTREE,
  CONSTRAINT `fk_company_auth_company` FOREIGN KEY (`company_id`) REFERENCES `company_info` (`enterprise_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_auth
-- ----------------------------

-- ----------------------------
-- Table structure for company_info
-- ----------------------------
DROP TABLE IF EXISTS `company_info`;
CREATE TABLE `company_info`  (
  `enterprise_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `industry` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `scale` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `website` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `auth_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PASS',
  `auth_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`enterprise_id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_company_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_info
-- ----------------------------
INSERT INTO `company_info` VALUES (1, 2, '成都校企科技有限公司', NULL, NULL, NULL, NULL, NULL, NULL, 'PASS', NULL, '2026-04-17 13:00:58', '2026-04-17 13:00:58');
INSERT INTO `company_info` VALUES (2, 5, '成都校企科技有限公司', NULL, NULL, NULL, NULL, NULL, NULL, 'PENDING', NULL, '2026-04-18 09:17:34', '2026-04-18 09:17:34');

-- ----------------------------
-- Table structure for enterprise_interview
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_interview`;
CREATE TABLE `enterprise_interview`  (
  `interview_id` bigint NOT NULL AUTO_INCREMENT,
  `apply_id` bigint NOT NULL,
  `enterprise_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `resume_id` bigint NOT NULL,
  `job_id` bigint NOT NULL,
  `interview_time` datetime NOT NULL,
  `interview_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `interview_place` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `interview_link` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contact_mobile` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`interview_id`) USING BTREE,
  INDEX `idx_enterprise_interview_enterprise`(`enterprise_id` ASC) USING BTREE,
  INDEX `idx_enterprise_interview_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_enterprise_interview_apply`(`apply_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enterprise_interview
-- ----------------------------
INSERT INTO `enterprise_interview` VALUES (1, 1, 1, 1, 1, 2, '2026-04-20 14:00:00', 'OFFLINE', '成都市高新区天府软件园', '', 'HR 李老师', '13900000000', '请提前 10 分钟到场', 'PENDING', '2026-04-17 21:25:59', '2026-04-17 21:25:59');

-- ----------------------------
-- Table structure for enterprise_job_refresh_log
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_job_refresh_log`;
CREATE TABLE `enterprise_job_refresh_log`  (
  `log_id` bigint NOT NULL AUTO_INCREMENT,
  `enterprise_id` bigint NOT NULL,
  `job_id` bigint NOT NULL,
  `refresh_date` date NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_job_refresh_date`(`job_id` ASC, `refresh_date` ASC) USING BTREE,
  INDEX `idx_enterprise_refresh_date`(`enterprise_id` ASC, `refresh_date` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enterprise_job_refresh_log
-- ----------------------------
INSERT INTO `enterprise_job_refresh_log` VALUES (1, 1, 1, '2026-04-17', '2026-04-17 17:45:23');
INSERT INTO `enterprise_job_refresh_log` VALUES (2, 1, 1, '2026-04-17', '2026-04-17 17:45:24');

-- ----------------------------
-- Table structure for enterprise_resume_favorite
-- ----------------------------
DROP TABLE IF EXISTS `enterprise_resume_favorite`;
CREATE TABLE `enterprise_resume_favorite`  (
  `favorite_id` bigint NOT NULL AUTO_INCREMENT,
  `enterprise_id` bigint NOT NULL,
  `resume_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`favorite_id`) USING BTREE,
  UNIQUE INDEX `uk_enterprise_resume_favorite`(`enterprise_id` ASC, `resume_id` ASC) USING BTREE,
  INDEX `idx_resume_favorite_enterprise`(`enterprise_id` ASC) USING BTREE,
  INDEX `idx_resume_favorite_resume`(`resume_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of enterprise_resume_favorite
-- ----------------------------
INSERT INTO `enterprise_resume_favorite` VALUES (1, 1, 1, '2026-04-18 09:15:07');

-- ----------------------------
-- Table structure for job_application
-- ----------------------------
DROP TABLE IF EXISTS `job_application`;
CREATE TABLE `job_application`  (
  `apply_id` bigint NOT NULL AUTO_INCREMENT,
  `job_id` bigint NOT NULL,
  `resume_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `enterprise_id` bigint NOT NULL,
  `cover_letter` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING',
  `apply_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`apply_id`) USING BTREE,
  UNIQUE INDEX `uk_job_user`(`job_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `fk_apply_resume`(`resume_id` ASC) USING BTREE,
  INDEX `fk_apply_user`(`user_id` ASC) USING BTREE,
  INDEX `fk_apply_enterprise`(`enterprise_id` ASC) USING BTREE,
  CONSTRAINT `fk_apply_enterprise` FOREIGN KEY (`enterprise_id`) REFERENCES `company_info` (`enterprise_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_apply_job` FOREIGN KEY (`job_id`) REFERENCES `job_position` (`job_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_apply_resume` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`resume_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_apply_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_application
-- ----------------------------
INSERT INTO `job_application` VALUES (1, 2, 1, 1, 1, NULL, 'UNSUITABLE', '2026-04-17 21:24:45', '2026-04-17 21:26:37');
INSERT INTO `job_application` VALUES (2, 1, 1, 1, 1, NULL, 'PENDING', '2026-04-18 14:31:40', '2026-04-18 14:31:40');

-- ----------------------------
-- Table structure for job_category
-- ----------------------------
DROP TABLE IF EXISTS `job_category`;
CREATE TABLE `job_category`  (
  `category_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint NOT NULL DEFAULT 0,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL DEFAULT 0,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `idx_category_parent`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_category
-- ----------------------------
INSERT INTO `job_category` VALUES (1, 0, '互联网技术', 'IT', 1, 'ACTIVE', '2026-04-17 13:33:06', '2026-04-17 13:33:06');
INSERT INTO `job_category` VALUES (2, 0, '智能制造', 'MANUFACTURE', 2, 'ACTIVE', '2026-04-17 13:33:06', '2026-04-17 13:33:06');
INSERT INTO `job_category` VALUES (3, 1, '后端开发', 'JAVA_BACKEND', 1, 'ACTIVE', '2026-04-17 13:33:06', '2026-04-17 13:33:06');
INSERT INTO `job_category` VALUES (4, 0, '软件工程', '软件工程', 3, 'ACTIVE', '2026-04-17 14:09:16', '2026-04-17 14:09:16');
INSERT INTO `job_category` VALUES (5, 0, '前端开发', '前端开发', 0, 'ACTIVE', '2026-04-17 16:56:44', '2026-04-17 16:56:44');

-- ----------------------------
-- Table structure for job_collection
-- ----------------------------
DROP TABLE IF EXISTS `job_collection`;
CREATE TABLE `job_collection`  (
  `collection_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `job_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`collection_id`) USING BTREE,
  UNIQUE INDEX `uk_job_collection_user_job`(`user_id` ASC, `job_id` ASC) USING BTREE,
  INDEX `idx_job_collection_user`(`user_id` ASC) USING BTREE,
  INDEX `idx_job_collection_job`(`job_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_collection
-- ----------------------------
INSERT INTO `job_collection` VALUES (1, 1, 2, '2026-04-17 23:43:52');

-- ----------------------------
-- Table structure for job_message
-- ----------------------------
DROP TABLE IF EXISTS `job_message`;
CREATE TABLE `job_message`  (
  `message_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `biz_id` bigint NULL DEFAULT NULL,
  `read_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'UNREAD',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `read_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `fk_message_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_message_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_message
-- ----------------------------
INSERT INTO `job_message` VALUES (1, 1, 'ACCOUNT', '账号状态已更新', '您的账号状态已被管理员调整为: DISABLED', 1, 'READ', '2026-04-17 16:26:21', '2026-04-17 16:55:26');
INSERT INTO `job_message` VALUES (2, 1, 'ACCOUNT', '账号状态已更新', '您的账号状态已被管理员调整为: ACTIVE', 1, 'READ', '2026-04-17 16:26:22', '2026-04-17 16:55:26');
INSERT INTO `job_message` VALUES (3, 2, 'AUDIT_RESULT', '职位审核结果通知', '职位《Java开发工程师》审核结果为: RECRUITING，原因: 职位内容审核通过', 2, 'UNREAD', '2026-04-17 21:24:27', NULL);
INSERT INTO `job_message` VALUES (4, 2, 'AUDIT_RESULT', '职位审核结果通知', '职位《Java开发工程师》审核结果为: RECRUITING，原因: 职位内容审核通过', 1, 'UNREAD', '2026-04-17 21:24:28', NULL);
INSERT INTO `job_message` VALUES (5, 2, 'APPLY', '收到新的简历投递', '默认简历 已投递到职位 Java开发工程师', 1, 'UNREAD', '2026-04-17 21:24:45', NULL);
INSERT INTO `job_message` VALUES (6, 1, 'SYSTEM', '企业主动沟通邀请', '成都校企科技有限公司 就职位《Java开发工程师》向您发来沟通消息：您好，我们对您的背景很感兴趣，想进一步沟通岗位机会。', 1, 'READ', '2026-04-17 21:24:58', '2026-04-17 21:25:15');
INSERT INTO `job_message` VALUES (7, 1, 'INTERVIEW', '收到新的面试邀约', 'Java开发工程师 面试时间：2026-04-20 14:00:00，面试地点：成都市高新区天府软件园', 1, 'READ', '2026-04-17 21:25:59', '2026-04-17 21:26:13');
INSERT INTO `job_message` VALUES (8, 1, 'REPLY', '简历状态已更新', '您的投递状态已更新为: UNSUITABLE，备注: 感谢投递，当前岗位暂不匹配', 1, 'READ', '2026-04-17 21:26:30', '2026-04-17 23:43:42');
INSERT INTO `job_message` VALUES (9, 1, 'REPLY', '简历状态已更新', '您的投递状态已更新为: UNSUITABLE，备注: 感谢投递，当前岗位暂不匹配', 1, 'READ', '2026-04-17 21:26:37', '2026-04-17 21:26:48');
INSERT INTO `job_message` VALUES (10, 2, 'APPLY', '收到新的简历投递', '默认简历 已投递到职位 Java开发工程师', 2, 'UNREAD', '2026-04-18 14:31:40', NULL);

-- ----------------------------
-- Table structure for job_notice
-- ----------------------------
DROP TABLE IF EXISTS `job_notice`;
CREATE TABLE `job_notice`  (
  `notice_id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'PENDING',
  `audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `publish_time` datetime NULL DEFAULT NULL,
  `create_by` bigint NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`notice_id`) USING BTREE,
  INDEX `fk_notice_creator`(`create_by` ASC) USING BTREE,
  INDEX `idx_notice_status_type`(`status` ASC, `type` ASC) USING BTREE,
  CONSTRAINT `fk_notice_creator` FOREIGN KEY (`create_by`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_notice
-- ----------------------------
INSERT INTO `job_notice` VALUES (1, '平台招聘周启动', '请各企业及时完善招聘信息，管理员将统一开展内容审核。', 'JOB_FAIR', 'ONLINE', '公告审核通过', '2026-04-17 23:44:43', 3, '2026-04-17 13:33:06', '2026-04-17 23:44:43');

-- ----------------------------
-- Table structure for job_position
-- ----------------------------
DROP TABLE IF EXISTS `job_position`;
CREATE TABLE `job_position`  (
  `job_id` bigint NOT NULL AUTO_INCREMENT,
  `enterprise_id` bigint NOT NULL,
  `job_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `job_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `responsibility` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `requirement_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `salary_min` int NOT NULL,
  `salary_max` int NOT NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `head_count` int NOT NULL,
  `education` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `experience` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `welfare` json NULL,
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `contact_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `view_count` int NOT NULL DEFAULT 0,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'RECRUITING',
  `audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `refresh_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`job_id`) USING BTREE,
  INDEX `fk_job_enterprise`(`enterprise_id` ASC) USING BTREE,
  CONSTRAINT `fk_job_enterprise` FOREIGN KEY (`enterprise_id`) REFERENCES `company_info` (`enterprise_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of job_position
-- ----------------------------
INSERT INTO `job_position` VALUES (1, 1, 'Java开发工程师', '后端开发', '负责招聘平台核心业务开发与维护。', '熟悉 Spring Boot、MySQL、Vue 基础协作流程。', 8000, 15000, '成都', 2, '本科', '1-3年', '[\"双休\", \"五险一金\"]', 'HR 李老师', '13900000000', 0, 'RECRUITING', '职位内容审核通过', '2026-04-17 16:05:25', '2026-04-17 17:45:24', '2026-04-17 16:05:25', '2026-04-17 21:24:28');
INSERT INTO `job_position` VALUES (2, 1, 'Java开发工程师', '后端开发', '负责招聘平台核心业务开发与维护。', '熟悉 Spring Boot、MySQL、Vue 基础协作流程。', 8000, 15000, '成都', 2, '本科', '1-3年', '[\"双休\", \"五险一金\"]', 'HR 李老师', '13900000000', 0, 'RECRUITING', '职位内容审核通过', '2026-04-17 17:47:06', '2026-04-17 17:47:06', '2026-04-17 17:47:06', '2026-04-17 21:24:27');

-- ----------------------------
-- Table structure for resume
-- ----------------------------
DROP TABLE IF EXISTS `resume`;
CREATE TABLE `resume`  (
  `resume_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `basic_info` json NOT NULL,
  `job_intention` json NOT NULL,
  `education_list` json NOT NULL,
  `work_list` json NULL,
  `skill_list` json NULL,
  `self_evaluation` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `privacy` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ENTERPRISE_ONLY',
  `is_default` tinyint(1) NOT NULL DEFAULT 0,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`resume_id`) USING BTREE,
  INDEX `fk_resume_user`(`user_id` ASC) USING BTREE,
  CONSTRAINT `fk_resume_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resume
-- ----------------------------
INSERT INTO `resume` VALUES (1, 1, '默认简历', '{\"name\": \"测试求职者\", \"email\": \"111@163.com\", \"gender\": \"MALE\", \"mobile\": \"1385678\", \"birthday\": \"2001-01-01\", \"currentCity\": \"成都\"}', '{\"expectCity\": \"成都\", \"expectIndustry\": \"互联网\", \"expectPosition\": \"Java开发工程师\", \"expectSalaryMax\": 15000, \"expectSalaryMin\": 8000}', '[{\"major\": \"计算机科学与技术\", \"degree\": \"本科\", \"school\": \"XX大学\", \"endDate\": \"2023-06\", \"startDate\": \"2019-09\"}]', '[]', '[]', '熟悉 Java / Spring Boot / Vue 全栈开发。', 'ENTERPRISE_ONLY', 1, '2026-04-17 13:33:37', '2026-04-17 17:46:50');

-- ----------------------------
-- Table structure for resume_attachment
-- ----------------------------
DROP TABLE IF EXISTS `resume_attachment`;
CREATE TABLE `resume_attachment`  (
  `attachment_id` bigint NOT NULL AUTO_INCREMENT,
  `resume_id` bigint NOT NULL,
  `file_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `uploader_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`attachment_id`) USING BTREE,
  INDEX `fk_resume_attachment_resume`(`resume_id` ASC) USING BTREE,
  INDEX `fk_resume_attachment_file`(`file_id` ASC) USING BTREE,
  CONSTRAINT `fk_resume_attachment_file` FOREIGN KEY (`file_id`) REFERENCES `sys_file_record` (`file_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_resume_attachment_resume` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`resume_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resume_attachment
-- ----------------------------

-- ----------------------------
-- Table structure for sys_audit_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_audit_log`;
CREATE TABLE `sys_audit_log`  (
  `audit_id` bigint NOT NULL AUTO_INCREMENT,
  `biz_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `biz_id` bigint NOT NULL,
  `before_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `after_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `audit_result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `audit_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `auditor_id` bigint NOT NULL,
  `auditor_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`audit_id`) USING BTREE,
  INDEX `fk_audit_user`(`auditor_id` ASC) USING BTREE,
  INDEX `idx_audit_biz`(`biz_type` ASC, `biz_id` ASC) USING BTREE,
  CONSTRAINT `fk_audit_user` FOREIGN KEY (`auditor_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_audit_log
-- ----------------------------
INSERT INTO `sys_audit_log` VALUES (1, 'JOB', 2, 'PENDING', 'RECRUITING', 'PASS', '职位内容审核通过', 3, '平台管理员', '2026-04-17 21:24:27');
INSERT INTO `sys_audit_log` VALUES (2, 'JOB', 1, 'PENDING', 'RECRUITING', 'PASS', '职位内容审核通过', 3, '平台管理员', '2026-04-17 21:24:28');
INSERT INTO `sys_audit_log` VALUES (3, 'NOTICE', 1, 'ONLINE', 'ONLINE', 'PASS', '公告审核通过', 3, '平台管理员', '2026-04-17 23:44:43');

-- ----------------------------
-- Table structure for sys_banner
-- ----------------------------
DROP TABLE IF EXISTS `sys_banner`;
CREATE TABLE `sys_banner`  (
  `banner_id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `image_file_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `sort` int NOT NULL DEFAULT 0,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ONLINE',
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`banner_id`) USING BTREE,
  INDEX `idx_sys_banner_status_sort`(`status` ASC, `sort` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_banner
-- ----------------------------
INSERT INTO `sys_banner` VALUES (1, '百度', '62d2d9a01b0e42e3bc33706d0cd4416c', '/uploads/62d2d9a01b0e42e3bc33706d0cd4416c.png', 'baidu.com', 0, 'ONLINE', '2026-01-01 00:00:00', '2027-01-01 00:00:00', '2026-04-17 17:46:17', '2026-04-17 17:46:17');

-- ----------------------------
-- Table structure for sys_export_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_export_task`;
CREATE TABLE `sys_export_task`  (
  `task_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `task_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_id` bigint NOT NULL,
  `file_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `download_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'SUCCESS',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`task_id`) USING BTREE,
  INDEX `idx_export_user_type`(`user_id` ASC, `task_type` ASC) USING BTREE,
  CONSTRAINT `fk_export_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_export_task
-- ----------------------------
INSERT INTO `sys_export_task` VALUES ('15e39403f3e74005ae56704122832a6f', 'PLATFORM', 3, '5fbf2be9e6ee4390a7635793b59c775b', '/uploads/reports/5fbf2be9e6ee4390a7635793b59c775b.csv', 'SUCCESS', '2026-04-17 15:48:07');
INSERT INTO `sys_export_task` VALUES ('1667c89fc7e44c12965c4ec57913c7e7', 'CANDIDATE', 1, 'da376f51e9da404a946fb0d5fcad7f5e', '/uploads/reports/da376f51e9da404a946fb0d5fcad7f5e.csv', 'SUCCESS', '2026-04-17 15:26:00');
INSERT INTO `sys_export_task` VALUES ('18034f05d4f847e59737b7291e5f72ae', 'PLATFORM', 3, '48984e7a3cff48baa18bb7be63438320', '/uploads/reports/48984e7a3cff48baa18bb7be63438320.xlsx', 'SUCCESS', '2026-04-17 16:57:15');
INSERT INTO `sys_export_task` VALUES ('2717b07ef75241019dfc124a7c8f1615', 'CANDIDATE', 1, '59bf8e4072394a37a17d5ac9de2dafdc', '/uploads/reports/59bf8e4072394a37a17d5ac9de2dafdc.xlsx', 'SUCCESS', '2026-04-17 17:06:08');
INSERT INTO `sys_export_task` VALUES ('5c7d5b359d804b269cb7a995b370094f', 'ENTERPRISE', 2, '818f0b11206d47159b47815726287b4c', '/uploads/reports/818f0b11206d47159b47815726287b4c.xlsx', 'SUCCESS', '2026-04-17 16:25:58');
INSERT INTO `sys_export_task` VALUES ('608e3812c72144a4ba365af8acacc57c', 'ENTERPRISE', 2, '1b9321f8ec474595a9c18606380d92c5', '/uploads/reports/1b9321f8ec474595a9c18606380d92c5.xlsx', 'SUCCESS', '2026-04-17 21:25:04');
INSERT INTO `sys_export_task` VALUES ('8dc4df6de43049ffa989d6a5e8be0e3c', 'CANDIDATE', 1, 'c05a3890b5bf469eac5a8a991fa4165e', '/uploads/reports/c05a3890b5bf469eac5a8a991fa4165e.csv', 'SUCCESS', '2026-04-17 15:46:51');
INSERT INTO `sys_export_task` VALUES ('8efbd60f9eba415c8a69c655b14e10b5', 'CANDIDATE', 1, 'a254e18c751f437c8b4e5030da530b14', '/uploads/reports/a254e18c751f437c8b4e5030da530b14.xlsx', 'SUCCESS', '2026-04-18 14:32:59');
INSERT INTO `sys_export_task` VALUES ('a21ec0acd020401787880058365fece7', 'CANDIDATE', 1, '7acd768630c446f5b143373c09178adf', '/uploads/reports/7acd768630c446f5b143373c09178adf.csv', 'SUCCESS', '2026-04-17 15:23:57');
INSERT INTO `sys_export_task` VALUES ('ae54892feb6b4b88a5e41e19422b7dde', 'CANDIDATE', 1, 'be73e3079abb4f5282e0276b732c82eb', '/uploads/reports/be73e3079abb4f5282e0276b732c82eb.csv', 'SUCCESS', '2026-04-17 14:45:40');
INSERT INTO `sys_export_task` VALUES ('e141ef3045c14f38a4cb0cd0a27fb962', 'CANDIDATE', 1, 'f3fc80b3612449c6a8462f9c93d89ed7', '/uploads/reports/f3fc80b3612449c6a8462f9c93d89ed7.csv', 'SUCCESS', '2026-04-17 14:47:25');

-- ----------------------------
-- Table structure for sys_file_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_record`;
CREATE TABLE `sys_file_record`  (
  `file_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `biz_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `file_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `file_size` bigint NOT NULL,
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `uploader_id` bigint NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`file_id`) USING BTREE,
  INDEX `fk_file_uploader`(`uploader_id` ASC) USING BTREE,
  INDEX `idx_file_biz_type`(`biz_type` ASC) USING BTREE,
  CONSTRAINT `fk_file_uploader` FOREIGN KEY (`uploader_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_file_record
-- ----------------------------
INSERT INTO `sys_file_record` VALUES ('0056b624f7b942afb69d4c8cb467cc0d', 'RESUME_EXPORT', 'enterprise-resumes-2026-04-18.pdf', '/uploads/reports/0056b624f7b942afb69d4c8cb467cc0d.pdf', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\0056b624f7b942afb69d4c8cb467cc0d.pdf', 2444, 'pdf', 2, '2026-04-18 10:51:43');
INSERT INTO `sys_file_record` VALUES ('1b9321f8ec474595a9c18606380d92c5', 'REPORT', 'statistics-enterprise-2026-04-17.xlsx', '/uploads/reports/1b9321f8ec474595a9c18606380d92c5.xlsx', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\1b9321f8ec474595a9c18606380d92c5.xlsx', 3536, 'xlsx', 2, '2026-04-17 21:25:04');
INSERT INTO `sys_file_record` VALUES ('2523ac30fd5e4584b6f480f383fcff4a', 'RESUME_EXPORT', '默认简历-1.pdf', '/uploads/reports/2523ac30fd5e4584b6f480f383fcff4a.pdf', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\2523ac30fd5e4584b6f480f383fcff4a.pdf', 2480, 'pdf', 1, '2026-04-18 14:33:33');
INSERT INTO `sys_file_record` VALUES ('48984e7a3cff48baa18bb7be63438320', 'REPORT', 'statistics-platform-2026-04-17.xlsx', '/uploads/reports/48984e7a3cff48baa18bb7be63438320.xlsx', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\48984e7a3cff48baa18bb7be63438320.xlsx', 3533, 'xlsx', 3, '2026-04-17 16:57:15');
INSERT INTO `sys_file_record` VALUES ('4e40e2b759994c96baead8b91f683a1c', 'RESUME_EXPORT', '默认简历-1.pdf', '/uploads/reports/4e40e2b759994c96baead8b91f683a1c.pdf', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\4e40e2b759994c96baead8b91f683a1c.pdf', 2480, 'pdf', 1, '2026-04-18 10:51:07');
INSERT INTO `sys_file_record` VALUES ('59bf8e4072394a37a17d5ac9de2dafdc', 'REPORT', 'statistics-candidate-2026-04-17.xlsx', '/uploads/reports/59bf8e4072394a37a17d5ac9de2dafdc.xlsx', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\59bf8e4072394a37a17d5ac9de2dafdc.xlsx', 3507, 'xlsx', 1, '2026-04-17 17:06:08');
INSERT INTO `sys_file_record` VALUES ('5d200e67c3424ac58d4a5bea45e29bac', 'RESUME_EXPORT', '默认简历-1.pdf', '/uploads/reports/5d200e67c3424ac58d4a5bea45e29bac.pdf', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\5d200e67c3424ac58d4a5bea45e29bac.pdf', 2480, 'pdf', 1, '2026-04-17 16:25:42');
INSERT INTO `sys_file_record` VALUES ('5fbf2be9e6ee4390a7635793b59c775b', 'REPORT', 'statistics-platform-2026-04-17.csv', '/uploads/reports/5fbf2be9e6ee4390a7635793b59c775b.csv', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\5fbf2be9e6ee4390a7635793b59c775b.csv', 92, 'csv', 3, '2026-04-17 15:48:07');
INSERT INTO `sys_file_record` VALUES ('62d2d9a01b0e42e3bc33706d0cd4416c', 'BANNER', '【哲风壁纸】卡通-海绵宝宝-清新.png', '/uploads/62d2d9a01b0e42e3bc33706d0cd4416c.png', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\62d2d9a01b0e42e3bc33706d0cd4416c.png', 562455, 'png', 3, '2026-04-17 17:46:15');
INSERT INTO `sys_file_record` VALUES ('7acd768630c446f5b143373c09178adf', 'REPORT', 'statistics-candidate-2026-04-17.csv', '/uploads/reports/7acd768630c446f5b143373c09178adf.csv', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\7acd768630c446f5b143373c09178adf.csv', 74, 'csv', 1, '2026-04-17 15:23:57');
INSERT INTO `sys_file_record` VALUES ('7b344e19a794499aaf960c15931e83ad', 'RESUME_EXPORT', '默认简历-1.pdf', '/uploads/reports/7b344e19a794499aaf960c15931e83ad.pdf', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\7b344e19a794499aaf960c15931e83ad.pdf', 2480, 'pdf', 1, '2026-04-17 17:06:14');
INSERT INTO `sys_file_record` VALUES ('818f0b11206d47159b47815726287b4c', 'REPORT', 'statistics-enterprise-2026-04-17.xlsx', '/uploads/reports/818f0b11206d47159b47815726287b4c.xlsx', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\818f0b11206d47159b47815726287b4c.xlsx', 3529, 'xlsx', 2, '2026-04-17 16:25:58');
INSERT INTO `sys_file_record` VALUES ('a254e18c751f437c8b4e5030da530b14', 'REPORT', 'statistics-candidate-2026-04-18.xlsx', '/uploads/reports/a254e18c751f437c8b4e5030da530b14.xlsx', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\a254e18c751f437c8b4e5030da530b14.xlsx', 3515, 'xlsx', 1, '2026-04-18 14:32:59');
INSERT INTO `sys_file_record` VALUES ('a638596e89c4443291d8da5f12cb1b80', 'RESUME_EXPORT', 'enterprise-resumes-2026-04-18.xlsx', '/uploads/reports/a638596e89c4443291d8da5f12cb1b80.xlsx', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\a638596e89c4443291d8da5f12cb1b80.xlsx', 3800, 'xlsx', 2, '2026-04-18 09:15:22');
INSERT INTO `sys_file_record` VALUES ('b2f3845593b1404194b132400cd4850f', 'RESUME_EXPORT', '默认简历-1.pdf', '/uploads/reports/b2f3845593b1404194b132400cd4850f.pdf', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\b2f3845593b1404194b132400cd4850f.pdf', 2480, 'pdf', 1, '2026-04-17 16:25:15');
INSERT INTO `sys_file_record` VALUES ('be73e3079abb4f5282e0276b732c82eb', 'REPORT', 'statistics-candidate-2026-04-17.csv', '/uploads/reports/be73e3079abb4f5282e0276b732c82eb.csv', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\be73e3079abb4f5282e0276b732c82eb.csv', 74, 'csv', 1, '2026-04-17 14:45:40');
INSERT INTO `sys_file_record` VALUES ('c05a3890b5bf469eac5a8a991fa4165e', 'REPORT', 'statistics-candidate-2026-04-17.csv', '/uploads/reports/c05a3890b5bf469eac5a8a991fa4165e.csv', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\c05a3890b5bf469eac5a8a991fa4165e.csv', 74, 'csv', 1, '2026-04-17 15:46:51');
INSERT INTO `sys_file_record` VALUES ('da376f51e9da404a946fb0d5fcad7f5e', 'REPORT', 'statistics-candidate-2026-04-17.csv', '/uploads/reports/da376f51e9da404a946fb0d5fcad7f5e.csv', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\da376f51e9da404a946fb0d5fcad7f5e.csv', 74, 'csv', 1, '2026-04-17 15:26:00');
INSERT INTO `sys_file_record` VALUES ('f3fc80b3612449c6a8462f9c93d89ed7', 'REPORT', 'statistics-candidate-2026-04-17.csv', '/uploads/reports/f3fc80b3612449c6a8462f9c93d89ed7.csv', 'D:\\Project\\XiaoQiHui\\backend\\uploads\\reports\\f3fc80b3612449c6a8462f9c93d89ed7.csv', 74, 'csv', 1, '2026-04-17 14:47:25');

-- ----------------------------
-- Table structure for sys_message_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_message_template`;
CREATE TABLE `sys_message_template`  (
  `template_id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `title_template` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `channels` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'INSITE,EMAIL',
  `enabled` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`template_id`) USING BTREE,
  UNIQUE INDEX `uk_template_type`(`type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_message_template
-- ----------------------------
INSERT INTO `sys_message_template` VALUES (1, 'AUDIT_RESULT', '审核结果通知', '您好，{targetName} 的审核结果为 {result}，请及时登录平台查看详情。', 'INSITE,EMAIL', 'ACTIVE', '2026-04-17 15:46:18', '2026-04-17 15:46:18');
INSERT INTO `sys_message_template` VALUES (2, 'INTERVIEW', '面试邀约通知', '您收到新的面试邀约，时间为 {interviewTime}，请尽快确认。', 'INSITE,EMAIL', 'ACTIVE', '2026-04-17 15:46:18', '2026-04-17 15:46:18');
INSERT INTO `sys_message_template` VALUES (3, 'LOGIN_CODE', '邮箱验证码', '您的登录验证码为 {code}，5 分钟内有效，请勿泄露给他人。', 'EMAIL', 'ACTIVE', '2026-04-17 15:46:18', '2026-04-17 15:46:18');

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `log_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `action` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `resource` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `idx_operation_user_time`(`user_id` ASC, `create_time` ASC) USING BTREE,
  INDEX `idx_operation_action_time`(`action` ASC, `create_time` ASC) USING BTREE,
  CONSTRAINT `fk_operation_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (1, 3, '平台管理员', 'NOTICE_AUDIT', 'notice:1', '公告审核通过', '0:0:0:0:0:0:0:1', '2026-04-17 23:44:43');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `permission_id` bigint NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `permission_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `menu_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`permission_id`) USING BTREE,
  UNIQUE INDEX `uk_permission_code`(`permission_code` ASC) USING BTREE,
  INDEX `idx_sys_permission_menu`(`menu_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (1, '用户管理', 'admin:user:manage', 'candidate', '查看并维护求职者和企业账号', '2026-04-17 15:46:18');
INSERT INTO `sys_permission` VALUES (2, '审核管理', 'admin:audit:manage', 'job-audit', '审核企业认证、职位和公告', '2026-04-17 15:46:18');
INSERT INTO `sys_permission` VALUES (3, '系统配置', 'admin:system:manage', 'banner', '维护轮播图、分类、消息模板与角色', '2026-04-17 15:46:18');
INSERT INTO `sys_permission` VALUES (4, '统计分析', 'admin:statistics:view', 'statistics', '查看平台统计与导出报表', '2026-04-17 15:46:18');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `uk_role_name`(`role_name` ASC) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code` ASC) USING BTREE,
  INDEX `idx_sys_role_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '平台管理员', 'PLATFORM_ADMIN', '默认全量后台管理角色', 'ACTIVE', '2026-04-17 15:46:18', '2026-04-17 15:46:18');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE,
  INDEX `fk_sys_role_permission_permission`(`permission_id` ASC) USING BTREE,
  CONSTRAINT `fk_sys_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`permission_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_sys_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`role_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (1, 1, '2026-04-17 15:46:18');
INSERT INTO `sys_role_permission` VALUES (1, 2, '2026-04-17 15:46:18');
INSERT INTO `sys_role_permission` VALUES (1, 3, '2026-04-17 15:46:18');
INSERT INTO `sys_role_permission` VALUES (1, 4, '2026-04-17 15:46:18');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `identity_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `user_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'ACTIVE',
  `current_city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `school` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `graduation_year` int NULL DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `mobile`(`mobile` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '13812345678', '$2a$10$Z.mtI0Ka8zGxhEOUlkSGCuN78wBZaVa32kgIhcXpa.cXL7jt11tWe', '测试求职者', '111@163.com', NULL, 'STUDENT', 'CANDIDATE', 'ACTIVE', '广州', '华南农业大学', '软件工程', NULL, '2026-04-17 13:00:30', '2026-04-17 16:26:22');
INSERT INTO `sys_user` VALUES (2, '13912345678', '$2a$10$UDJpjIkwekupacmO3PZm9.7YoR65TCvxB7sBk7JGaWH7cqJawbk3.', '企业联系人', '222@163.com', NULL, 'ENTERPRISE', 'ENTERPRISE', 'ACTIVE', NULL, NULL, NULL, NULL, '2026-04-17 13:00:58', '2026-04-17 15:20:53');
INSERT INTO `sys_user` VALUES (3, '18800000000', '$2a$10$IHI.bSIYNFrMnEBC4xw.tunjvW2zs9FejM8/CkFOPrM/Nh21Krxqi', '平台管理员', '333@163.com', NULL, 'ADMIN', 'ADMIN', 'ACTIVE', NULL, NULL, NULL, NULL, '2026-04-17 13:33:06', '2026-04-17 15:20:57');
INSERT INTO `sys_user` VALUES (4, '17328973361', '$2a$10$71woIjZ3POekGeWcC1JEoeRUYL7Nbo9iTB0PvWYenT2DWMX93ljUy', '测试求职者', '891440539@qq.com', NULL, 'STUDENT', 'CANDIDATE', 'ACTIVE', NULL, NULL, NULL, NULL, '2026-04-18 09:13:55', '2026-04-18 09:13:55');
INSERT INTO `sys_user` VALUES (5, '17328973362', '$2a$10$CvuCosYF1Bp0PjzMCkUM9eElgEb/JXxYZ.yB.LiEbca3qaU.xeFDu', '企业联系人', '891440540@qq.com', NULL, 'ENTERPRISE', 'ENTERPRISE', 'ACTIVE', NULL, NULL, NULL, NULL, '2026-04-18 09:17:34', '2026-04-18 09:17:34');
INSERT INTO `sys_user` VALUES (6, '13812345674', '$2a$10$NlJw21.7.CSVYMVHAGURTuZzUdvFQj2cq3mDP33SyqLFH9M7uRNva', '测试求职者', 'candida@example.com', NULL, 'STUDENT', 'CANDIDATE', 'ACTIVE', NULL, NULL, NULL, NULL, '2026-04-18 10:50:39', '2026-04-18 10:50:39');

SET FOREIGN_KEY_CHECKS = 1;
