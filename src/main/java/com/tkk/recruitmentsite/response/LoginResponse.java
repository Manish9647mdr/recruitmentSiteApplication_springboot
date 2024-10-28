package com.tkk.recruitmentsite.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private Long expireIn;
}
