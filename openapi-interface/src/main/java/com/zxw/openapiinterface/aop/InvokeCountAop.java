package com.zxw.openapiinterface.aop;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author zxw
 * 调用次数切面
 */
@RestControllerAdvice
public class InvokeCountAop {
    //伪代码
    //定义切面触发时机，接口的方法执行成功后，执行下述方法
//    @AfterReturning("execution(* com.zxw.openapiinterface.controller.*.*(..))")
//    public void afterReturning() {
//        //调用方法
//        object.proceed();
//        //成功后次数+1
//        invokeCountService.invokeCount();
//
//    }

}
