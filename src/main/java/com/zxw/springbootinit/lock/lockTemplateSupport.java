package com.zxw.springbootinit.lock;

import org.apache.poi.ss.formula.functions.T;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * 加锁的模板代码
 *
 * @author MECHREVO
 */
public class lockTemplateSupport {
    private final ReentrantLock lock = new ReentrantLock();

    public void executeWithMethod(long timeout, TimeUnit unit, Runnable task) throws InterruptedException {
        boolean locked = lock.tryLock(1, TimeUnit.SECONDS);
        if (!locked) {
            System.out.println("获取锁失败");
        }
        try {
            //加锁成功
//            createMethod();
            task.run();
        } finally {
            lock.unlock();
        }
    }

    public <T> T executeWithMethod2(long timeout, TimeUnit unit, Supplier<T> task) throws InterruptedException {
        boolean locked = lock.tryLock(1, TimeUnit.SECONDS);
        if (!locked) {
            return null;
        }
        try {
            //加锁成功
//            createMethod();
            return task.get();
        } finally {
            lock.unlock();
        }
    }

    public void createMethod() {
        System.out.println("do sth...");
    }
}
