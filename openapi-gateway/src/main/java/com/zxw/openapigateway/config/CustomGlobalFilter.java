package com.zxw.openapigateway.config;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zxw.openapiclientsdk.utils.SignUtils;
import com.zxw.openapicommon.model.entity.InterfaceInfo;
import com.zxw.openapicommon.model.entity.User;
import com.zxw.openapicommon.service.InnerInterfaceInfoService;
import com.zxw.openapicommon.service.InnerUserInterfaceInfoService;
import com.zxw.openapicommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author MECHREVO
 * 全局过滤
 * Ordered 作用是定义过滤器的执行顺序
 * 全局过滤器 - 成功拦截所有进入该网关的请求
 * 完成以下功能:
 * 1 - 用户发送请求到 API 网关
 * 2 - 请求日志
 * 3 - (黑白名单）
 * 4 - 用户鉴权 (判断 ak,sk 是否合法)
 * 5 - 请求的模拟接口是否存在
 * 6 - 请求转发，调用模拟接口
 * 7 - 响应日志
 * 8 - 调用成功，接口调用次数 + 1
 * 9 - 调用失败，返回一个规范的错误码
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;
    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;
    //白名单
    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");
    private static final String INTERFACE_HOST = "http://localhost:8123";


    // 对应的 `CustomGlobalHandler` 函数需要位于 `ServerHttpResponseUtils` 类中，并且必须为 static 函数.
    //定义资源，并提供可选的异常处理和 fallback 配置项
    @SentinelResource(
            value = "/api/name/user",
            blockHandler = "CustomGlobalHandler",
            blockHandlerClass = CustomGlobalHandler.class
    )
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1. 用户发送请求到 api 网关
//        2. 请求日志
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("请求路径：" + path);
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        log.info("请求头：" + request.getHeaders());
        log.info("请求体：" + request.getBody());
        log.info("请求唯一标识：" + request.getId());
        log.info("请求来源地址：" + request.getLocalAddress().getHostString());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + request.getRemoteAddress());
//        3. 访问控制，黑白名单
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
//        4. 用户鉴权（判断 ak，sk 是否合法）
//       //从请求头中校验
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String body = headers.getFirst("body");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
//      实际情况是从数据库中查是否已分配给用户,注意sk传递不出来
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            return handleNoAuth(response);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        //校验时间戳,时间和当前时间不能超过五分钟
        if (isWithinFiveMinutes(Long.parseLong(timestamp))) {
            return handleNoAuth(response);
        }
        //校验签名, 实际从数据库中取出secretKey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }

//        if (!accessKey.equals("123")) {
//            return handleNoAuth(response);
//        }
//        //校验随机数
//        if (Long.parseLong(nonce) > 100000) {
//            return handleNoAuth(response);
//        }
//        5. 请求的模拟接口是否存在？参数是否合法？
        //实际情况从数据库中查模拟接口是否存在,以及请求方法是否匹配（还可以校验请求参数）
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            return handleNoAuth(response);
        }
        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }
//         todo 检验是否还有调用次数
//        boolean hasRemainingCalls;
//        try {
//            hasRemainingCalls = innerUserInterfaceInfoService.hasRemainingCalls(invokeUser.getId(), interfaceInfo.getId());
//        } catch (Exception e) {
//            log.error("检查调用次数异常", e);
//            return handleInvokeError(response);
//        }
//        if (!hasRemainingCalls) {
//            log.warn("用户调用次数已用完");
//            return handleNoAuth(response);
//        }
//            使用远程调用dubbo数据库接口完成调用
//        6. 请求转发，调用模拟接口
//        Mono<Void> filter = chain.filter(exchange);
//        响应日志
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
//        7. 调用成功，接口调用次数加一 ，调用backend中的invokeCount方法
//        if (response.getStatusCode() == HttpStatus.OK) {
//
//        } else {
////            8. 调用失败，返回一个规范的错误码
//            return handleInvokeError(response);
//        }
//        return filter;
    }

    /**
     * 处理响应
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceInfoId, Long UserId) {
        try {
            log.info("custom global filter");
            // 显式定义上下文,统一簇点链路
            ContextUtil.enter("/api/name/user");

            // 1. 请求转发，调用模拟接口
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 缓存数据的工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                    fluxBody.map(dataBuffer -> {
                                        // 7.  调用成功，接口调用次数 + 1 invokeCount
                                        try {
                                            innerUserInterfaceInfoService.invokeCount(interfaceInfoId, UserId);
                                        } catch (Exception e) {
                                            log.error("调用次数加一异常" + e);
                                        }
                                        byte[] content = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(content);
                                        DataBufferUtils.release(dataBuffer);//释放掉内存
                                        // 构建日志
                                        StringBuilder sb2 = new StringBuilder(200);
                                        List<Object> rspArgs = new ArrayList<>();
                                        rspArgs.add(originalResponse.getStatusCode());
                                        String data = new String(content, StandardCharsets.UTF_8); //data
                                        sb2.append(data);
                                        // 打印日志
                                        log.info("响应结果：" + data);
                                        return bufferFactory.wrap(content);
                                    }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }

    /**
     * Ordered 作用是定义过滤器的执行顺序
     *
     * @return
     */
    @Override
    public int getOrder() {
        return -1;
    }

    /**
     * 校验时间戳是否在五分钟内
     *
     * @param timestamp 待校验的时间戳
     * @return 如果时间戳与当前时间的差值不超过五分钟，则返回 true；否则返回 false
     */
    public boolean isWithinFiveMinutes(long timestamp) {
        Instant now = Instant.now();
        Instant inputInstant = Instant.ofEpochMilli(timestamp);
        long diffInSeconds = Math.abs(now.toEpochMilli() - inputInstant.toEpochMilli()) / 1000;
        // 5 分钟换算成秒
        return diffInSeconds <= 300;
    }

    /**
     * 权限未通过
     *
     * @param response
     * @return
     */

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

}