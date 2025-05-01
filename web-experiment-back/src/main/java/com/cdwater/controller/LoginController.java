package com.cdwater.controller;

import com.cdwater.pojo.Admin;
import com.cdwater.pojo.LoginInfo;
import com.cdwater.pojo.Result;
import com.cdwater.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        log.info("管理员登录：{}", admin);
        LoginInfo loginInfo = adminService.login(admin);
        if (loginInfo != null) {
            return Result.success(loginInfo);
        }
        return Result.error("用户名或密码错误");
    }

    @PostMapping("/register")
    public Result register(@RequestBody Admin admin) {
        log.info("管理员注册：{}", admin);
        adminService.register(admin);
        return Result.success();
    }

    @PutMapping("/forget")
    public Result forget(@RequestBody Admin admin) {
        log.info("管理员忘记密码：{}", admin);
        Integer responseType = adminService.forget(admin);
        if (responseType == 0) {
            return Result.error("用户名不存在");
        } else if (responseType == 1) {
            return Result.error("两次新密码输入不一致");
        } else if (responseType == 2) {
            return Result.error("邮箱验证不通过");
        } else if (responseType == 3) {
            log.info("忘记密码->修改成功，新密码：{}", admin.getNewPassword());
            return Result.success();
        }
        return Result.success();
    }
}
