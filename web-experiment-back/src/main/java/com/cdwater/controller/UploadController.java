package com.cdwater.controller;

import com.cdwater.pojo.Result;
import com.cdwater.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@Slf4j
public class UploadController {
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传：{}", file.getOriginalFilename());
        //将文件交给OSS存储管理
        String url = aliyunOSSOperator.upload(file.getBytes(), Objects.requireNonNull(file.getOriginalFilename()));
        log.info("文件上传OSS，url：{}", url);
        return Result.success(url);
    }
}
