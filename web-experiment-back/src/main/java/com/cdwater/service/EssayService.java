package com.cdwater.service;

import com.cdwater.pojo.Essay;
import com.cdwater.pojo.EssayQueryParam;
import com.cdwater.pojo.PageResult;

public interface EssayService {
    void insertEssay(Essay essay);

    void deleteEssay(Integer id,Integer adminId);

    void updateEssay(Essay essay);

    PageResult<Essay> selectEssayByPage(EssayQueryParam essayQueryParam);

    Essay selectEssayById(Integer id);
}
