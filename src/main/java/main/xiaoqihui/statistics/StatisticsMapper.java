package main.xiaoqihui.statistics;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StatisticsMapper {

    @Select("select count(1) from job_application where user_id = #{userId}")
    long countCandidateApplies(Long userId);

    @Select("select count(1) from job_application where user_id = #{userId} and status = 'INTERVIEW'")
    long countCandidateInterviews(Long userId);

    @Select("select count(1) from job_application where user_id = #{userId} and status != 'PENDING'")
    long countCandidateViews(Long userId);

    @Select("""
        select date_format(apply_time, '%Y-%m-%d') as day, count(1) as count
        from job_application
        where user_id = #{userId} and apply_time >= #{startDate}
        group by date_format(apply_time, '%Y-%m-%d')
        order by day asc
        """)
    List<DailyCount> candidateApplyTrend(@Param("userId") Long userId, @Param("startDate") LocalDate startDate);

    @Select("""
        select coalesce(sum(view_count), 0) from job_position
        where enterprise_id = #{enterpriseId}
        """)
    long sumEnterpriseJobViews(Long enterpriseId);

    @Select("select count(1) from job_application where enterprise_id = #{enterpriseId}")
    long countEnterpriseApplies(Long enterpriseId);

    @Select("select count(1) from job_application where enterprise_id = #{enterpriseId} and status = 'INTERVIEW'")
    long countEnterpriseInterviews(Long enterpriseId);

    @Select("select count(1) from job_position where enterprise_id = #{enterpriseId} and status = 'RECRUITING'")
    long countEnterpriseActiveJobs(Long enterpriseId);

    @Select("""
        select jp.job_id, jp.job_name, jp.status, jp.view_count,
               count(ja.apply_id) as apply_count,
               sum(case when ja.status = 'INTERVIEW' then 1 else 0 end) as interview_count
        from job_position jp
        left join job_application ja on ja.job_id = jp.job_id
        where jp.enterprise_id = #{enterpriseId}
        group by jp.job_id, jp.job_name, jp.status, jp.view_count
        order by jp.create_time desc
        """)
    List<JobStatRow> listEnterpriseJobStats(Long enterpriseId);

    @Select("""
        select date_format(apply_time, '%Y-%m-%d') as day,
               count(1) as apply_count,
               sum(case when status = 'INTERVIEW' then 1 else 0 end) as interview_count
        from job_application
        where enterprise_id = #{enterpriseId}
          and apply_time >= #{startDate}
          and apply_time <= #{endDate}
        group by date_format(apply_time, '%Y-%m-%d')
        order by day asc
        """)
    List<EnterpriseTrendRow> enterpriseTrend(
        @Param("enterpriseId") Long enterpriseId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Select("select count(1) from sys_user where user_type = 'CANDIDATE'")
    long countTotalCandidates();

    @Select("select count(1) from company_info")
    long countTotalEnterprises();

    @Select("select count(1) from job_position")
    long countTotalJobs();

    @Select("select count(1) from job_application")
    long countTotalApplies();

    @Select("select count(1) from job_application where status = 'INTERVIEW'")
    long countTotalInterviews();

    @Select("""
        select ifnull(current_city, '未填写') as area, count(1) as count
        from sys_user
        where user_type = 'CANDIDATE'
        group by ifnull(current_city, '未填写')
        order by count desc
        limit 10
        """)
    List<AreaDistRow> employmentByArea();

    @Select("""
        select ifnull(ci.industry, '未填写') as industry,
               count(distinct jp.job_id) as job_count,
               count(ja.apply_id) as apply_count
        from company_info ci
        left join job_position jp on jp.enterprise_id = ci.enterprise_id
        left join job_application ja on ja.job_id = jp.job_id
        group by ifnull(ci.industry, '未填写')
        order by apply_count desc, job_count desc
        limit 10
        """)
    List<IndustryDistRow> industryDistribution();

    @Select("""
        select ifnull(education, '未填写') as education, count(1) as count
        from job_position
        group by ifnull(education, '未填写')
        order by count desc
        """)
    List<EducationDistRow> educationDistribution();

    @Select("""
        select date_format(apply_time, '%Y-%m') as day, count(1) as count
        from job_application
        where apply_time >= #{startDate}
          and apply_time <= #{endDate}
        group by date_format(apply_time, '%Y-%m')
        order by day asc
        """)
    List<DailyCount> platformTrend(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Insert("""
        insert into sys_export_task (task_id, task_type, user_id, file_id, download_url, status)
        values (#{taskId}, #{taskType}, #{userId}, #{fileId}, #{downloadUrl}, #{status})
        """)
    int insertExportTask(ExportTaskEntity task);
}
