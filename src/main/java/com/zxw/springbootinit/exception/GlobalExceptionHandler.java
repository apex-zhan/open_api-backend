package com.zxw.springbootinit.exception;

import com.zxw.springbootinit.common.BaseResponse;
import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @RestControllerAdvice 环绕切面
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 自定义的业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    /**
     * 运行时异常
     * @param e
     * @return
     */

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
