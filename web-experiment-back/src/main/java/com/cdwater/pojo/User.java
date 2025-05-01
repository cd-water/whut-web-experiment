package com.cdwater.pojo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String name;
    private String province;
    private String city;
    private String address;
    private String zip;
    private LocalDateTime updateTime;
}
