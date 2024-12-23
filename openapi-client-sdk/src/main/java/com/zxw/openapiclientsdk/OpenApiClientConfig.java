package com.zxw.openapiclientsdk;

import com.zxw.openapiclientsdk.client.openApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * @author zxw
 */
@Configuration
@ConfigurationProperties("openapi.client")
@Data
@ComponentScan
public class OpenApiClientConfig {
    private String accessKey;
    private String secretKey;

    @Bean
    public openApiClient openApiClient(){
        return new openApiClient(accessKey,secretKey);
    }
}
