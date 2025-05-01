package com.cdwater.controller;

import com.cdwater.pojo.*;
import com.cdwater.service.EssayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/essays")
public class EssayController {
    @Autowired
    private EssayService essayService;

    @PostMapping
    public Result insertEssay(@RequestBody Essay essay) {
        log.info("新增文章：{}", essay);
        essayService.insertEssay(essay);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result deleteEssay(@PathVariable Integer id, @RequestParam Integer adminId) {
        log.info("删除文章：文章id->{},作者id->{}", id, adminId);
        essayService.deleteEssay(id, adminId);
        return Result.success();
    }

    @PutMapping
    public Result updateEssay(@RequestBody Essay essay) {
        log.info("修改文章：{}", essay);
        essayService.updateEssay(essay);
        return Result.success();
    }

    @GetMapping
    public Result selectEssayByPage(EssayQueryParam essayQueryParam) {
        log.info("分页查询文章：{}", essayQueryParam);
        PageResult<Essay> pageResult = essayService.selectEssayByPage(essayQueryParam);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result selectEssayById(@PathVariable Integer id) {
        log.info("ID查询文章：{}", id);
        Essay essay = essayService.selectEssayById(id);
        return Result.success(essay);
    }
}
