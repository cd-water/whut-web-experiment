package com.cdwater.service;

import com.cdwater.pojo.Admin;
import com.cdwater.pojo.AdminQueryParam;
import com.cdwater.pojo.LoginInfo;
import com.cdwater.pojo.PageResult;

public interface AdminService {
    Admin selectAdminByUsername(String username);

    void updateAdminInfo(Admin admin);

    Integer updateAdminPassword(Admin admin);

    PageResult<Admin> selectAdminByPage(AdminQueryParam adminQueryParam);

    LoginInfo login(Admin admin);

    void register(Admin admin);

    Integer forget(Admin admin);
}
