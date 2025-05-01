package com.cdwater.pojo;

import lombok.Data;

@Data
public class AdminQueryParam {
    private Integer page=1;
    private Integer pageSize=10;
}
