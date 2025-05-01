package com.cdwater.service.impl;

import com.cdwater.mapper.AdminMapper;
import com.cdwater.pojo.*;
import com.cdwater.service.AdminService;
import com.cdwater.utils.JwtUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin selectAdminByUsername(String username) {
        return adminMapper.selectAdminByUsername(username);
    }

    @Override
    public void updateAdminInfo(Admin admin) {
        adminMapper.updateAdminInfo(admin);
    }

    @Override
    public Integer updateAdminPassword(Admin admin) {
        Admin adminSaved = adminMapper.selectAdminByUsername(admin.getUsername());
        //两次新密码输入不一致
        if (!admin.getNewPassword().equals(admin.getConfirmPassword())) {
            return 0;
        }
        //旧密码输入错误
        if (!adminSaved.getPassword().equals(admin.getOldPassword())) {
            return 1;
        }
        adminMapper.updateAdminPassword(admin);
        return 2;
    }

    @Override
    public PageResult<Admin> selectAdminByPage(AdminQueryParam adminQueryParam) {
        PageHelper.startPage(adminQueryParam.getPage(), adminQueryParam.getPageSize());
        List<Admin> adminList = adminMapper.selectAdminByPage(adminQueryParam);
        Page<Admin> p = (Page<Admin>) adminList;
        return new PageResult<Admin>(p.getTotal(), p.getResult());
    }

    @Override
    public LoginInfo login(Admin admin) {
        Admin adminResult = adminMapper.login(admin);
        if (adminResult != null) {
            log.info("登录成功，管理员信息：{}", adminResult);
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", adminResult.getId());
            claims.put("username", adminResult.getUsername());
            String jwt = JwtUtils.generateToken(claims);
            return new LoginInfo(adminResult.getId(), adminResult.getUsername(), jwt);
        }
        return null;
    }

    @Override
    public void register(Admin admin) {
        if (admin.getBirthday() == null) {
            admin.setBirthday(LocalDate.now());
        }
        if (admin.getAvatar() == null || admin.getAvatar().isEmpty()) {
            admin.setAvatar("https://cdwater-bucket.oss-cn-beijing.aliyuncs.com/avatar.jpg");
        }
        adminMapper.register(admin);
    }

    @Override
    public Integer forget(Admin admin) {
        Admin adminSaved = adminMapper.selectAdminByUsername(admin.getUsername());
        //用户不存在
        if (adminSaved == null) {
            return 0;
        }
        //两次新密码输入不一致
        if (!admin.getNewPassword().equals(admin.getConfirmPassword())) {
            return 1;
        }
        //邮箱验证不通过
        if (!adminSaved.getEmail().equals(admin.getEmail())) {
            return 2;
        }
        adminMapper.updateAdminPassword(admin);
        return 3;
    }
}
