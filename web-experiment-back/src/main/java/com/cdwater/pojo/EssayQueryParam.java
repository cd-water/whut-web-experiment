package com.cdwater.pojo;

import lombok.Data;

@Data
public class EssayQueryParam {
    private Integer page=1;
    private Integer pageSize=10;
    private String title;
    private Integer adminId;
}
