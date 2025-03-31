package com.zxw.springbootinit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 电子邮件配置
 */
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class EmailConfig {
    // 邮箱账号
    private String emailFrom = "3327332153@qq.com";
}
