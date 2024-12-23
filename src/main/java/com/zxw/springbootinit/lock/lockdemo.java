package com.zxw.springbootinit.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author MECHREVO
 */
public class lockdemo {
    private final ReentrantLock lock = new ReentrantLock();
//    @Autowired

    private final lockTemplateSupport lockTemplateSupport = new lockTemplateSupport();

    public void syncMethod2() throws InterruptedException {
        lockTemplateSupport.executeWithMethod(1, TimeUnit.SECONDS, this::coreMethod);

    }


    public void syncMethod1() throws InterruptedException {

        boolean locked = lock.tryLock(1, TimeUnit.SECONDS);
        if (!locked) {
            System.out.println("获取锁失败");
        }
        try {
            //加锁成功
            coreMethod();
        } finally {
            lock.unlock();
        }
    }

    @lockMethod(timeout = 1, unit = TimeUnit.SECONDS)
    public void coreMethod() {
        System.out.println("do sth...");
    }

}
