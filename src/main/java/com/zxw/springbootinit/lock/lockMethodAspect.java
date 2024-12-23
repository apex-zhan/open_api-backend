package com.zxw.springbootinit.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author MECHREVO
 */
@Aspect
@Component
@Order(1)
public class lockMethodAspect {
    @Pointcut("execution (* com.zxw.springbootinit.lock.lockMethod.*(..))")
    public void lockMethodPointcut() {

    }

    @Around("lockMethodPointcut()")
    public Object lockMethodAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //
        return joinPoint.proceed();
    }
}
