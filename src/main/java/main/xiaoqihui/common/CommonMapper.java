package main.xiaoqihui.common;

import main.xiaoqihui.common.domain.DictItemResponse;
import main.xiaoqihui.common.domain.FileRecordEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommonMapper {

    @Insert("""
        insert into sys_file_record (
            file_id, biz_type, original_name, file_url, file_path, file_size, file_type, uploader_id
        ) values (
            #{fileId}, #{bizType}, #{originalName}, #{fileUrl}, #{filePath}, #{fileSize}, #{fileType}, #{uploaderId}
        )
        """)
    int insertFileRecord(FileRecordEntity entity);

    @Select("select * from sys_file_record where file_id = #{fileId} limit 1")
    FileRecordEntity findFileById(String fileId);

    @Select("""
        select code, name, sort
        from job_category
        where parent_id = 0 and status = 'ACTIVE'
        order by sort asc, category_id asc
        """)
    List<DictItemResponse> listJobCategories();

    @Select("""
        <script>
        select code, name, sort
        from job_category
        where status = 'ACTIVE'
        <if test="parentId != null">
            and parent_id = #{parentId}
        </if>
        order by sort asc, category_id asc
        </script>
        """)
    List<DictItemResponse> listCategoriesByParent(@Param("parentId") Long parentId);
}
