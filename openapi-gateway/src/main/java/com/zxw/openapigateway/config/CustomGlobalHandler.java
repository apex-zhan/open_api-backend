package com.zxw.openapigateway.config;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zxw.openapigateway.utils.ServerHttpResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 自定义全局异常处理类
 */
@Slf4j
@Component
public class CustomGlobalHandler {
    //参数类型需要和原方法相匹配并且最后加一个额外的参数，类型为 BlockException
    //可以指定 blockHandlerClass 为对应的类的 Class 对象，注意对应的函数必需为 static 函数，否则无法解析
    public static Mono<Void> invokeInterfaceHandler(ServerWebExchange exchange,
                                                    GatewayFilterChain chain,
                                                    BlockException e) {
        //返回响应
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpResponseUtils.blockBySentinelException(response);
        ServerHttpRequest request = exchange.getRequest();
        String id = request.getId();
        String pathValue = request.getPath().value();
        String methodName = request.getMethod().name();
        String hostStr = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
        //打印错误日志
        log.error("请求id：{} uri:{} 请求方法:{} 请求来源:{} 过于频繁，限流 :{}",
                id, pathValue, methodName, hostStr, e.getMessage());
        return response.setComplete();
    }
}