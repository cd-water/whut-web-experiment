package com.cdwater.service.impl;

import com.cdwater.mapper.EssayMapper;
import com.cdwater.pojo.Essay;
import com.cdwater.pojo.EssayQueryParam;
import com.cdwater.pojo.PageResult;
import com.cdwater.service.EssayService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EssayServiceImpl implements EssayService {
    @Autowired
    private EssayMapper essayMapper;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void insertEssay(Essay essay) {
        essayMapper.insertEssay(essay);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void deleteEssay(Integer id,Integer adminId) {
        essayMapper.deleteEssay(id,adminId);
    }

    @Override
    public void updateEssay(Essay essay) {
        essayMapper.updateEssay(essay);
    }

    @Override
    public PageResult<Essay> selectEssayByPage(EssayQueryParam essayQueryParam) {
        PageHelper.startPage(essayQueryParam.getPage(), essayQueryParam.getPageSize());
        List<Essay> essayList = essayMapper.selectEssayByPage(essayQueryParam);
        Page<Essay> p = (Page<Essay>) essayList;
        return new PageResult<Essay>(p.getTotal(), p.getResult());
    }

    @Override
    public Essay selectEssayById(Integer id) {
        return essayMapper.selectEssayById(id);
    }
}
