package com.cdwater.pojo;

import lombok.Data;

@Data
public class Essay {
    private Integer id;
    private String title;
    private String content;
    private Integer adminId;
}
