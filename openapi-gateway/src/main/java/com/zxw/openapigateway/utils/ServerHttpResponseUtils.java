package com.zxw.openapigateway.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

/**
 * 自定义全局异常处理类
 */
@Slf4j
@Component
public class ServerHttpResponseUtils {
    public static void internelServerError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //限流异常 - 请求过于频繁
    public static void blockBySentinelException(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
    }
}
