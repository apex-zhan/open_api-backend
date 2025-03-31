package com.zxw.springbootinit.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮箱登录请求体
 * @author MECHREVO
 */
@Data
public class UserEmailLoginRequest implements Serializable {
    private String EmailAccount;
    private String captcha;
    private static final long serialVersionUID = 3191241716373120793L;


}
