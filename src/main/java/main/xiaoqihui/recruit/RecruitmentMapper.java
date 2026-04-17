package main.xiaoqihui.recruit;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RecruitmentMapper {

    @Select("select * from sys_user where mobile = #{mobile} limit 1")
    UserEntity findUserByMobile(String mobile);

    @Select("select * from sys_user where user_id = #{userId} limit 1")
    UserEntity findUserById(Long userId);

    @Update("update sys_user set password = #{password}, update_time = now() where user_id = #{userId}")
    int updateUserPassword(@Param("userId") Long userId, @Param("password") String password);

    @Insert("""
        insert into sys_user (
            mobile, password, real_name, email, avatar, identity_type, user_type, status,
            current_city, school, major, graduation_year
        ) values (
            #{mobile}, #{password}, #{realName}, #{email}, #{avatar}, #{identityType}, #{userType}, #{status},
            #{currentCity}, #{school}, #{major}, #{graduationYear}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertUser(UserEntity user);

    @Update("""
        <script>
        update sys_user
        <set>
            <if test="avatar != null">avatar = #{avatar},</if>
            <if test="email != null">email = #{email},</if>
            <if test="school != null">school = #{school},</if>
            <if test="major != null">major = #{major},</if>
            <if test="graduationYear != null">graduation_year = #{graduationYear},</if>
            <if test="currentCity != null">current_city = #{currentCity},</if>
            update_time = now()
        </set>
        where user_id = #{userId}
        </script>
        """)
    int updateUserProfile(UserEntity user);

    @Insert("""
        insert into company_info (
            user_id, company_name, industry, scale, address, introduction, website, logo, auth_status
        ) values (
            #{userId}, #{companyName}, #{industry}, #{scale}, #{address}, #{introduction}, #{website}, #{logo}, #{authStatus}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "enterpriseId")
    int insertCompanyInfo(CompanyInfoEntity companyInfo);

    @Select("select * from company_info where user_id = #{userId} limit 1")
    CompanyInfoEntity findCompanyByUserId(Long userId);

    @Select("select enterprise_id from company_info where user_id = #{userId} limit 1")
    Long findCompanyIdByUserId(Long userId);

    @Select("select * from company_info where enterprise_id = #{enterpriseId} limit 1")
    CompanyInfoEntity findCompanyById(Long enterpriseId);

    @Select("select * from company_auth where company_id = #{companyId} order by auth_id desc limit 1")
    CompanyAuthEntity findLatestCompanyAuth(Long companyId);

    @Insert("""
        insert into company_auth (
            company_id, credit_code, legal_person, license_file_id, license_image, logo_file_id, logo_url,
            apply_time, audit_status
        ) values (
            #{companyId}, #{creditCode}, #{legalPerson}, #{licenseFileId}, #{licenseImage}, #{logoFileId}, #{logoUrl},
            #{applyTime}, #{auditStatus}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "authId")
    int insertCompanyAuth(CompanyAuthEntity auth);

    @Update("""
        update company_auth
        set credit_code = #{creditCode},
            legal_person = #{legalPerson},
            license_file_id = #{licenseFileId},
            license_image = #{licenseImage},
            logo_file_id = #{logoFileId},
            logo_url = #{logoUrl},
            apply_time = #{applyTime},
            audit_status = #{auditStatus},
            audit_remark = null,
            audit_time = null,
            auditor_id = null,
            update_time = now()
        where auth_id = #{authId}
        """)
    int updateCompanyAuth(CompanyAuthEntity auth);

    @Update("""
        <script>
        update company_info
        <set>
            <if test="companyName != null">company_name = #{companyName},</if>
            <if test="industry != null">industry = #{industry},</if>
            <if test="scale != null">scale = #{scale},</if>
            <if test="address != null">address = #{address},</if>
            <if test="introduction != null">introduction = #{introduction},</if>
            <if test="website != null">website = #{website},</if>
            <if test="logo != null">logo = #{logo},</if>
            <if test="authStatus != null">auth_status = #{authStatus},</if>
            <if test="authRemark != null">auth_remark = #{authRemark},</if>
            update_time = now()
        </set>
        where enterprise_id = #{enterpriseId}
        </script>
        """)
    int updateCompanyInfo(CompanyInfoEntity companyInfo);

    @Insert("""
        insert into resume (
            user_id, title, basic_info, job_intention, education_list, work_list, skill_list,
            self_evaluation, privacy, is_default
        ) values (
            #{userId}, #{title}, #{basicInfo}, #{jobIntention}, #{educationList}, #{workList}, #{skillList},
            #{selfEvaluation}, #{privacy}, #{isDefault}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "resumeId")
    int insertResume(ResumeEntity resume);

    @Update("""
        update resume
        set title = #{title},
            basic_info = #{basicInfo},
            job_intention = #{jobIntention},
            education_list = #{educationList},
            work_list = #{workList},
            skill_list = #{skillList},
            self_evaluation = #{selfEvaluation},
            privacy = #{privacy},
            update_time = now()
        where resume_id = #{resumeId} and user_id = #{userId}
        """)
    int updateResume(ResumeEntity resume);

    @Select("select * from resume where resume_id = #{resumeId} limit 1")
    ResumeEntity findResumeById(Long resumeId);

    @Select("select * from resume where user_id = #{userId} order by is_default desc, update_time desc")
    List<ResumeEntity> findResumesByUserId(Long userId);

    @Update("update resume set is_default = 0 where user_id = #{userId}")
    int clearDefaultResume(Long userId);

    @Update("update resume set is_default = 1 where resume_id = #{resumeId} and user_id = #{userId}")
    int setDefaultResume(@Param("userId") Long userId, @Param("resumeId") Long resumeId);

    @Insert("""
        insert into job_position (
            enterprise_id, job_name, job_category, responsibility, requirement_text,
            salary_min, salary_max, location, head_count, education, experience,
            welfare, contact_name, contact_mobile, status, publish_time, refresh_time
        ) values (
            #{enterpriseId}, #{jobName}, #{jobCategory}, #{responsibility}, #{requirementText},
            #{salaryMin}, #{salaryMax}, #{location}, #{headCount}, #{education}, #{experience},
            #{welfare}, #{contactName}, #{contactMobile}, #{status}, #{publishTime}, #{refreshTime}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "jobId")
    int insertJob(JobEntity job);

    @Update("""
        update job_position
        set job_name = #{jobName},
            job_category = #{jobCategory},
            responsibility = #{responsibility},
            requirement_text = #{requirementText},
            salary_min = #{salaryMin},
            salary_max = #{salaryMax},
            location = #{location},
            head_count = #{headCount},
            education = #{education},
            experience = #{experience},
            welfare = #{welfare},
            contact_name = #{contactName},
            contact_mobile = #{contactMobile},
            status = #{status},
            refresh_time = now(),
            audit_remark = null,
            update_time = now()
        where job_id = #{jobId} and enterprise_id = #{enterpriseId}
        """)
    int updateJob(JobEntity job);

    @Select("""
        select jp.*, ci.company_name, ci.logo as company_logo, ci.industry as company_industry
        from job_position jp
        join company_info ci on ci.enterprise_id = jp.enterprise_id
        where jp.job_id = #{jobId}
        limit 1
        """)
    JobEntity findJobById(Long jobId);

    @Select("""
        <script>
        select jp.*, ci.company_name, ci.logo as company_logo, ci.industry as company_industry
        from job_position jp
        join company_info ci on ci.enterprise_id = jp.enterprise_id
        where jp.status = 'RECRUITING'
        <if test="keyword != null and keyword != ''">
            and (jp.job_name like concat('%', #{keyword}, '%')
            or ci.company_name like concat('%', #{keyword}, '%')
            or jp.job_category like concat('%', #{keyword}, '%'))
        </if>
        <if test="education != null and education != ''">
            and jp.education = #{education}
        </if>
        <if test="experience != null and experience != ''">
            and jp.experience = #{experience}
        </if>
        <if test="location != null and location != ''">
            and jp.location like concat('%', #{location}, '%')
        </if>
        order by jp.refresh_time desc, jp.publish_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<JobEntity> searchJobs(
        @Param("keyword") String keyword,
        @Param("education") String education,
        @Param("experience") String experience,
        @Param("location") String location,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1)
        from job_position jp
        join company_info ci on ci.enterprise_id = jp.enterprise_id
        where jp.status = 'RECRUITING'
        <if test="keyword != null and keyword != ''">
            and (jp.job_name like concat('%', #{keyword}, '%')
            or ci.company_name like concat('%', #{keyword}, '%')
            or jp.job_category like concat('%', #{keyword}, '%'))
        </if>
        <if test="education != null and education != ''">
            and jp.education = #{education}
        </if>
        <if test="experience != null and experience != ''">
            and jp.experience = #{experience}
        </if>
        <if test="location != null and location != ''">
            and jp.location like concat('%', #{location}, '%')
        </if>
        </script>
        """)
    long countJobs(
        @Param("keyword") String keyword,
        @Param("education") String education,
        @Param("experience") String experience,
        @Param("location") String location
    );

    @Select("""
        <script>
        select * from job_position
        where enterprise_id = #{enterpriseId}
        <if test="status != null and status != ''">
            and status = #{status}
        </if>
        <if test="keyword != null and keyword != ''">
            and job_name like concat('%', #{keyword}, '%')
        </if>
        order by refresh_time desc, publish_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<JobEntity> listEnterpriseJobs(
        @Param("enterpriseId") Long enterpriseId,
        @Param("status") String status,
        @Param("keyword") String keyword,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1) from job_position
        where enterprise_id = #{enterpriseId}
        <if test="status != null and status != ''">
            and status = #{status}
        </if>
        <if test="keyword != null and keyword != ''">
            and job_name like concat('%', #{keyword}, '%')
        </if>
        </script>
        """)
    long countEnterpriseJobs(
        @Param("enterpriseId") Long enterpriseId,
        @Param("status") String status,
        @Param("keyword") String keyword
    );

    @Select("select count(1) from job_application where job_id = #{jobId} and user_id = #{userId}")
    long countUserApplication(@Param("jobId") Long jobId, @Param("userId") Long userId);

    @Select("select count(1) from job_application where job_id = #{jobId}")
    int countApplicationsByJob(Long jobId);

    @Select("select count(1) from job_application where enterprise_id = #{enterpriseId} and resume_id = #{resumeId}")
    long countEnterpriseResumeAccess(@Param("enterpriseId") Long enterpriseId, @Param("resumeId") Long resumeId);

    @Insert("""
        insert into job_application (
            job_id, resume_id, user_id, enterprise_id, cover_letter, status
        ) values (
            #{jobId}, #{resumeId}, #{userId}, #{enterpriseId}, #{coverLetter}, #{status}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "applyId")
    int insertApplication(ApplicationEntity application);

    @Select("""
        <script>
        select ja.*, jp.job_name, ci.company_name
        from job_application ja
        join job_position jp on jp.job_id = ja.job_id
        join company_info ci on ci.enterprise_id = ja.enterprise_id
        where ja.user_id = #{userId}
        <if test="status != null and status != ''">
            and ja.status = #{status}
        </if>
        order by ja.apply_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<ApplicationEntity> listUserApplications(
        @Param("userId") Long userId,
        @Param("status") String status,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1)
        from job_application ja
        where ja.user_id = #{userId}
        <if test="status != null and status != ''">
            and ja.status = #{status}
        </if>
        </script>
        """)
    long countUserApplications(@Param("userId") Long userId, @Param("status") String status);

    @Select("""
        <script>
        select ja.*, jp.job_name, su.real_name as candidate_name
        from job_application ja
        join job_position jp on jp.job_id = ja.job_id
        join sys_user su on su.user_id = ja.user_id
        where ja.enterprise_id = #{enterpriseId}
        <if test="status != null and status != ''">
            and ja.status = #{status}
        </if>
        <if test="jobId != null">
            and ja.job_id = #{jobId}
        </if>
        order by ja.apply_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<ApplicationEntity> listEnterpriseApplications(
        @Param("enterpriseId") Long enterpriseId,
        @Param("status") String status,
        @Param("jobId") Long jobId,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1)
        from job_application
        where enterprise_id = #{enterpriseId}
        <if test="status != null and status != ''">
            and status = #{status}
        </if>
        <if test="jobId != null">
            and job_id = #{jobId}
        </if>
        </script>
        """)
    long countEnterpriseApplications(
        @Param("enterpriseId") Long enterpriseId,
        @Param("status") String status,
        @Param("jobId") Long jobId
    );

    @Select("select * from job_application where apply_id = #{applyId} limit 1")
    ApplicationEntity findApplicationById(Long applyId);

    @Update("update job_application set status = #{status}, update_time = now() where apply_id = #{applyId}")
    int updateApplicationStatus(@Param("applyId") Long applyId, @Param("status") String status);

    @Insert("""
        insert into job_message (user_id, type, title, content, biz_id, read_status)
        values (#{userId}, #{type}, #{title}, #{content}, #{bizId}, #{readStatus})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "messageId")
    int insertMessage(MessageEntity message);

    @Select("""
        <script>
        select * from job_message
        where user_id = #{userId}
        <if test="type != null and type != ''">
            and type = #{type}
        </if>
        <if test="readStatus != null and readStatus != ''">
            and read_status = #{readStatus}
        </if>
        order by create_time desc
        limit #{limit} offset #{offset}
        </script>
        """)
    List<MessageEntity> listMessages(
        @Param("userId") Long userId,
        @Param("type") String type,
        @Param("readStatus") String readStatus,
        @Param("offset") int offset,
        @Param("limit") int limit
    );

    @Select("""
        <script>
        select count(1)
        from job_message
        where user_id = #{userId}
        <if test="type != null and type != ''">
            and type = #{type}
        </if>
        <if test="readStatus != null and readStatus != ''">
            and read_status = #{readStatus}
        </if>
        </script>
        """)
    long countMessages(
        @Param("userId") Long userId,
        @Param("type") String type,
        @Param("readStatus") String readStatus
    );

    @Select("select count(1) from job_message where user_id = #{userId} and read_status = 'UNREAD'")
    int countUnreadMessages(Long userId);

    @Update("""
        <script>
        update job_message
        set read_status = 'READ', read_time = now()
        where user_id = #{userId}
        and read_status = 'UNREAD'
        <if test="messageIds != null and messageIds.size() > 0">
            and message_id in
            <foreach collection="messageIds" item="id" open="(" separator="," close=")">
                #{id}
            </foreach>
        </if>
        </script>
        """)
    int markMessagesRead(@Param("userId") Long userId, @Param("messageIds") List<Long> messageIds);
}
