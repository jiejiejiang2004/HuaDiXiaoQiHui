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
}
