package com.cdwater.controller;

import com.cdwater.pojo.Admin;
import com.cdwater.pojo.AdminQueryParam;
import com.cdwater.pojo.PageResult;
import com.cdwater.pojo.Result;
import com.cdwater.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/{username}")
    public Result selectAdminByUsername(@PathVariable String username) {
        log.info("用户名查询管理员：{}", username);
        Admin admin = adminService.selectAdminByUsername(username);
        return Result.success(admin);
    }

    @PutMapping("/info")
    public Result updateAdminInfo(@RequestBody Admin admin) {
        log.info("编辑管理员信息：{}", admin);
        adminService.updateAdminInfo(admin);
        return Result.success();
    }

    @PutMapping("/password")
    public Result updateAdminPassword(@RequestBody Admin admin) {
        log.info("修改管理员密码：{}", admin);
        Integer responseType = adminService.updateAdminPassword(admin);
        if (responseType == 0) {
            return Result.error("两次新密码输入不一致");
        } else if (responseType == 1) {
            return Result.error("旧密码输入错误");
        } else if (responseType == 2) {
            log.info("修改密码->修改成功，新密码：{}", admin.getNewPassword());
            return Result.success();
        }
        return Result.success();
    }

    @GetMapping
    public Result selectAdminByPage(AdminQueryParam adminQueryParam){
        log.info("分页查询管理员：{}",adminQueryParam);
        PageResult<Admin> pageResult=adminService.selectAdminByPage(adminQueryParam);
        return Result.success(pageResult);
    }
}
