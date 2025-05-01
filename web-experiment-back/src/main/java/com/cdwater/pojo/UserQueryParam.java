package com.cdwater.pojo;

import lombok.Data;

@Data
public class UserQueryParam {
    private Integer page=1;
    private Integer pageSize=10;
    private String name;
}
