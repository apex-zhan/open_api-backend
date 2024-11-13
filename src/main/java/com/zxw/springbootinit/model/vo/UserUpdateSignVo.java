package com.zxw.springbootinit.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更改用户公钥私钥
 */
@Data
public class UserUpdateSignVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String accessKey;
    private String secretKey;
}
