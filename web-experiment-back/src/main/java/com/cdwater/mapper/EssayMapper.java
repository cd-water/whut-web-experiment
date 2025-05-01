package com.cdwater.mapper;

import com.cdwater.pojo.Essay;
import com.cdwater.pojo.EssayQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface EssayMapper {
    void insertEssay(Essay essay);

    void deleteEssay(Integer id,Integer adminId);

    @Update("update essay set title=#{title},content=#{content} where id=#{id}")
    void updateEssay(Essay essay);

    List<Essay> selectEssayByPage(EssayQueryParam essayQueryParam);

    @Select("select id, admin_id, title, content from essay where id=#{id}")
    Essay selectEssayById(Integer id);
}
