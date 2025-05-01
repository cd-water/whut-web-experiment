package com.cdwater.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginInfo {
    private Integer id;
    private String username;
    private String token;
}
