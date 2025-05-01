package com.cdwater.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Admin {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private String phone;
    private String email;
    private LocalDate birthday;
    private Integer money;
    private String address;
    private String avatar;
    private Integer essayCount;

    //用于修改密码
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
