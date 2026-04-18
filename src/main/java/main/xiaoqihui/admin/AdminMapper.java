package main.xiaoqihui.admin;

import main.xiaoqihui.recruit.CompanyAuthEntity;
import main.xiaoqihui.recruit.UserEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminMapper {

    @Select("select * from sys_user where mobile = #{mobile} limit 1")
    UserEntity findUserByMobile(String mobile);

    @Select("""
        <script>
        select user_id, mobile, real_name, identity_type, status, school, major, current_city, create_time
        from sys_user
        where user_type = 'CANDIDATE'
        <if test="keyword != null and keyword != ''">
            and (real_name like concat('%', #{keyword}, '%') or mobile like concat('%', #{keyword}, '%'))
        </if>
        <if test="identity != null and identity != ''">
            and identity_type = #{identity}
        </if>
        <if test="status != null and status != ''">
            and status = #{status}
        </if>
        order by create_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<AdminCandidateView> listCandidates(
        @Param("keyword") String keyword,
        @Param("identity") String identity,
        @Param("status") String status,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1)
        from sys_user
        where user_type = 'CANDIDATE'
        <if test="keyword != null and keyword != ''">
            and (real_name like concat('%', #{keyword}, '%') or mobile like concat('%', #{keyword}, '%'))
        </if>
        <if test="identity != null and identity != ''">
            and identity_type = #{identity}
        </if>
        <if test="status != null and status != ''">
            and status = #{status}
        </if>
        </script>
        """)
    long countCandidates(@Param("keyword") String keyword, @Param("identity") String identity, @Param("status") String status);

    @Select("select * from sys_user where user_id = #{userId} and user_type = 'CANDIDATE' limit 1")
    UserEntity findCandidateById(Long userId);

    @Update("update sys_user set status = #{status}, update_time = now() where user_id = #{userId}")
    int updateUserStatus(@Param("userId") Long userId, @Param("status") String status);

    @Select("""
        <script>
        select ci.enterprise_id, ci.user_id, ci.company_name, ci.industry, ci.scale, ci.address, ci.introduction,
               ci.auth_status, ci.auth_remark, su.status, su.mobile as contact_mobile, ci.create_time
        from company_info ci
        join sys_user su on su.user_id = ci.user_id
        where 1 = 1
        <if test="keyword != null and keyword != ''">
            and ci.company_name like concat('%', #{keyword}, '%')
        </if>
        <if test="authStatus != null and authStatus != ''">
            and ci.auth_status = #{authStatus}
        </if>
        <if test="status != null and status != ''">
            and su.status = #{status}
        </if>
        order by ci.create_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<AdminEnterpriseView> listEnterprises(
        @Param("keyword") String keyword,
        @Param("authStatus") String authStatus,
        @Param("status") String status,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1)
        from company_info ci
        join sys_user su on su.user_id = ci.user_id
        where 1 = 1
        <if test="keyword != null and keyword != ''">
            and ci.company_name like concat('%', #{keyword}, '%')
        </if>
        <if test="authStatus != null and authStatus != ''">
            and ci.auth_status = #{authStatus}
        </if>
        <if test="status != null and status != ''">
            and su.status = #{status}
        </if>
        </script>
        """)
    long countEnterprises(
        @Param("keyword") String keyword,
        @Param("authStatus") String authStatus,
        @Param("status") String status
    );

    @Select("""
        select ci.enterprise_id, ci.user_id, ci.company_name, ci.industry, ci.scale, ci.address, ci.introduction,
               ci.auth_status, ci.auth_remark, su.status, su.mobile as contact_mobile, ci.create_time
        from company_info ci
        join sys_user su on su.user_id = ci.user_id
        where ci.enterprise_id = #{enterpriseId}
        limit 1
        """)
    AdminEnterpriseView findEnterpriseById(Long enterpriseId);

    @Select("select * from company_auth where company_id = #{companyId} order by auth_id desc limit 1")
    CompanyAuthEntity findLatestCompanyAuth(Long companyId);

    @Update("""
        <script>
        update company_info
        <set>
            <if test="companyName != null">company_name = #{companyName},</if>
            <if test="industry != null">industry = #{industry},</if>
            <if test="scale != null">scale = #{scale},</if>
            <if test="address != null">address = #{address},</if>
            <if test="introduction != null">introduction = #{introduction},</if>
            <if test="authStatus != null">auth_status = #{authStatus},</if>
            <if test="authRemark != null">auth_remark = #{authRemark},</if>
            update_time = now()
        </set>
        where enterprise_id = #{enterpriseId}
        </script>
        """)
    int updateEnterprise(AdminEnterpriseView enterpriseView);

    @Update("""
        update company_auth
        set audit_status = #{auditStatus},
            audit_remark = #{auditRemark},
            audit_time = now(),
            auditor_id = #{auditorId},
            update_time = now()
        where auth_id = #{authId}
        """)
    int updateCompanyAuthAudit(
        @Param("authId") Long authId,
        @Param("auditStatus") String auditStatus,
        @Param("auditRemark") String auditRemark,
        @Param("auditorId") Long auditorId
    );

    @Select("""
        <script>
        select jp.job_id, jp.enterprise_id, ci.company_name, jp.job_name, jp.job_category,
               jp.location, jp.salary_min, jp.salary_max, jp.status, jp.audit_remark, jp.create_time
        from job_position jp
        join company_info ci on ci.enterprise_id = jp.enterprise_id
        where 1 = 1
        <if test="status != null and status != ''">
            <choose>
                <when test="status == 'PASSED'">
                    and jp.status = 'RECRUITING'
                </when>
                <when test="status == 'REJECTED'">
                    and jp.status = 'REJECTED'
                </when>
                <otherwise>
                    and jp.status = 'PENDING'
                </otherwise>
            </choose>
        </if>
        <if test="companyName != null and companyName != ''">
            and ci.company_name like concat('%', #{companyName}, '%')
        </if>
        order by jp.create_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<AdminJobAuditView> listAuditJobs(
        @Param("status") String status,
        @Param("companyName") String companyName,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1)
        from job_position jp
        join company_info ci on ci.enterprise_id = jp.enterprise_id
        where 1 = 1
        <if test="status != null and status != ''">
            <choose>
                <when test="status == 'PASSED'">
                    and jp.status = 'RECRUITING'
                </when>
                <when test="status == 'REJECTED'">
                    and jp.status = 'REJECTED'
                </when>
                <otherwise>
                    and jp.status = 'PENDING'
                </otherwise>
            </choose>
        </if>
        <if test="companyName != null and companyName != ''">
            and ci.company_name like concat('%', #{companyName}, '%')
        </if>
        </script>
        """)
    long countAuditJobs(@Param("status") String status, @Param("companyName") String companyName);

    @Select("""
        select jp.job_id, jp.enterprise_id, ci.company_name, jp.job_name, jp.job_category,
               jp.location, jp.salary_min, jp.salary_max, jp.status, jp.audit_remark, jp.create_time
        from job_position jp
        join company_info ci on ci.enterprise_id = jp.enterprise_id
        where jp.job_id = #{jobId}
        limit 1
        """)
    AdminJobAuditView findAuditJobById(Long jobId);

    @Update("update job_position set status = #{status}, audit_remark = #{auditRemark}, update_time = now() where job_id = #{jobId}")
    int updateJobAudit(@Param("jobId") Long jobId, @Param("status") String status, @Param("auditRemark") String auditRemark);

    @Select("""
        <script>
        select * from job_notice
        where 1 = 1
        <if test="status != null and status != ''">
            <choose>
                <when test="status == 'PASSED'">
                    and status = 'ONLINE'
                </when>
                <otherwise>
                    and status = #{status}
                </otherwise>
            </choose>
        </if>
        <if test="type != null and type != ''">
            and type = #{type}
        </if>
        order by create_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<AdminNoticeEntity> listNotices(
        @Param("status") String status,
        @Param("type") String type,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1) from job_notice
        where 1 = 1
        <if test="status != null and status != ''">
            <choose>
                <when test="status == 'PASSED'">
                    and status = 'ONLINE'
                </when>
                <otherwise>
                    and status = #{status}
                </otherwise>
            </choose>
        </if>
        <if test="type != null and type != ''">
            and type = #{type}
        </if>
        </script>
        """)
    long countNotices(@Param("status") String status, @Param("type") String type);

    @Select("select * from job_notice where notice_id = #{noticeId} limit 1")
    AdminNoticeEntity findNoticeById(Long noticeId);

    @Insert("""
        insert into job_notice (title, content, type, status, publish_time, create_by)
        values (#{title}, #{content}, #{type}, #{status}, #{publishTime}, #{createBy})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "noticeId")
    int insertNotice(AdminNoticeEntity notice);

    @Update("""
        <script>
        update job_notice
        <set>
            <if test="title != null">title = #{title},</if>
            <if test="content != null">content = #{content},</if>
            <if test="type != null">type = #{type},</if>
            <if test="status != null">status = #{status},</if>
            <if test="auditRemark != null">audit_remark = #{auditRemark},</if>
            <if test="publishTime != null">publish_time = #{publishTime},</if>
            update_time = now()
        </set>
        where notice_id = #{noticeId}
        </script>
        """)
    int updateNotice(AdminNoticeEntity notice);

    @Delete("delete from job_notice where notice_id = #{noticeId}")
    int deleteNotice(Long noticeId);

    @Select("""
        select * from job_category
        where parent_id = #{parentId}
        order by sort asc, category_id asc
        """)
    List<AdminCategoryEntity> listCategories(Long parentId);

    @Insert("""
        insert into job_category (parent_id, name, code, sort, status)
        values (#{parentId}, #{name}, #{code}, #{sort}, #{status})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "categoryId")
    int insertCategory(AdminCategoryEntity category);

    @Update("""
        update job_category
        set name = #{name}, sort = #{sort}, update_time = now()
        where category_id = #{categoryId}
        """)
    int updateCategory(AdminCategoryEntity category);

    @Delete("delete from job_category where category_id = #{categoryId}")
    int deleteCategory(Long categoryId);

    @Insert("""
        insert into sys_audit_log (biz_type, biz_id, before_status, after_status, audit_result, audit_remark, auditor_id, auditor_name)
        values (#{bizType}, #{bizId}, #{beforeStatus}, #{afterStatus}, #{auditResult}, #{auditRemark}, #{auditorId}, #{auditorName})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "auditId")
    int insertAuditLog(AuditLogEntity log);

    @Select("""
        select * from sys_audit_log
        order by create_time desc
        limit #{limit} offset #{offset}
        """)
    List<AuditLogEntity> listAuditLogs(@Param("offset") int offset, @Param("limit") int limit);

    @Select("select count(1) from sys_audit_log")
    long countAuditLogs();

    @Insert("""
        insert into sys_operation_log (user_id, user_name, action, resource, detail, ip)
        values (#{userId}, #{userName}, #{action}, #{resource}, #{detail}, #{ip})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "logId")
    int insertOperationLog(OperationLogEntity log);

    @Select("""
        <script>
        select * from sys_operation_log
        where 1 = 1
        <if test="userId != null">
            and user_id = #{userId}
        </if>
        <if test="action != null and action != ''">
            and action = #{action}
        </if>
        <if test="startTime != null and startTime != ''">
            and create_time <![CDATA[>=]]> #{startTime}
        </if>
        <if test="endTime != null and endTime != ''">
            and create_time <![CDATA[<=]]> #{endTime}
        </if>
        order by create_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<OperationLogEntity> listOperationLogs(
        @Param("userId") Long userId,
        @Param("action") String action,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1)
        from sys_operation_log
        where 1 = 1
        <if test="userId != null">
            and user_id = #{userId}
        </if>
        <if test="action != null and action != ''">
            and action = #{action}
        </if>
        <if test="startTime != null and startTime != ''">
            and create_time <![CDATA[>=]]> #{startTime}
        </if>
        <if test="endTime != null and endTime != ''">
            and create_time <![CDATA[<=]]> #{endTime}
        </if>
        </script>
        """)
    long countOperationLogs(
        @Param("userId") Long userId,
        @Param("action") String action,
        @Param("startTime") String startTime,
        @Param("endTime") String endTime
    );

    @Select("""
        select banner_id, title, image_file_id, image_url, link_url, sort, status, start_time, end_time, create_time, update_time
        from sys_banner
        order by sort asc, banner_id desc
        """)
    List<AdminBannerEntity> listBanners();

    @Select("""
        select banner_id, title, image_file_id, image_url, link_url, sort, status, start_time, end_time, create_time, update_time
        from sys_banner
        where banner_id = #{bannerId}
        limit 1
        """)
    AdminBannerEntity findBannerById(Long bannerId);

    @Insert("""
        insert into sys_banner (title, image_file_id, image_url, link_url, sort, status, start_time, end_time)
        values (#{title}, #{imageFileId}, #{imageUrl}, #{linkUrl}, #{sort}, #{status}, #{startTime}, #{endTime})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "bannerId")
    int insertBanner(AdminBannerEntity banner);

    @Update("""
        <script>
        update sys_banner
        <set>
            <if test="title != null">title = #{title},</if>
            <if test="imageFileId != null">image_file_id = #{imageFileId},</if>
            <if test="imageUrl != null">image_url = #{imageUrl},</if>
            <if test="linkUrl != null">link_url = #{linkUrl},</if>
            <if test="sort != null">sort = #{sort},</if>
            <if test="status != null">status = #{status},</if>
            start_time = #{startTime},
            end_time = #{endTime},
            update_time = now()
        </set>
        where banner_id = #{bannerId}
        </script>
        """)
    int updateBanner(AdminBannerEntity banner);

    @Delete("delete from sys_banner where banner_id = #{bannerId}")
    int deleteBanner(Long bannerId);

    @Select("""
        select role_id, role_name, role_code, remark, status, create_time, update_time
        from sys_role
        order by role_id asc
        """)
    List<AdminRoleEntity> listRoles();

    @Select("""
        select role_id, role_name, role_code, remark, status, create_time, update_time
        from sys_role
        where role_id = #{roleId}
        limit 1
        """)
    AdminRoleEntity findRoleById(Long roleId);

    @Insert("""
        insert into sys_role (role_name, role_code, remark, status)
        values (#{roleName}, #{roleCode}, #{remark}, #{status})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "roleId")
    int insertRole(AdminRoleEntity role);

    @Update("""
        update sys_role
        set role_name = #{roleName},
            role_code = #{roleCode},
            remark = #{remark},
            status = #{status},
            update_time = now()
        where role_id = #{roleId}
        """)
    int updateRole(AdminRoleEntity role);

    @Delete("delete from sys_role where role_id = #{roleId}")
    int deleteRole(Long roleId);

    @Select("""
        select permission_id, permission_name, permission_code, menu_key, description, create_time
        from sys_permission
        order by permission_id asc
        """)
    List<AdminPermissionEntity> listPermissions();

    @Select("select permission_id from sys_role_permission where role_id = #{roleId} order by permission_id asc")
    List<Long> listRolePermissionIds(Long roleId);

    @Delete("delete from sys_role_permission where role_id = #{roleId}")
    int deleteRolePermissions(Long roleId);

    @Insert("insert into sys_role_permission (role_id, permission_id) values (#{roleId}, #{permissionId})")
    int insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    @Select("""
        <script>
        select template_id, type, title_template, content_template, channels, enabled, create_time, update_time
        from sys_message_template
        <if test="type != null and type != ''">
            where type = #{type}
        </if>
        order by template_id asc
        </script>
        """)
    List<AdminMessageTemplateEntity> listMessageTemplates(@Param("type") String type);

    @Select("""
        select template_id, type, title_template, content_template, channels, enabled, create_time, update_time
        from sys_message_template
        where template_id = #{templateId}
        limit 1
        """)
    AdminMessageTemplateEntity findMessageTemplateById(Long templateId);

    @Update("""
        <script>
        update sys_message_template
        <set>
            <if test="titleTemplate != null">title_template = #{titleTemplate},</if>
            <if test="contentTemplate != null">content_template = #{contentTemplate},</if>
            <if test="channels != null">channels = #{channels},</if>
            <if test="enabled != null">enabled = #{enabled},</if>
            update_time = now()
        </set>
        where template_id = #{templateId}
        </script>
        """)
    int updateMessageTemplate(AdminMessageTemplateEntity template);
}
