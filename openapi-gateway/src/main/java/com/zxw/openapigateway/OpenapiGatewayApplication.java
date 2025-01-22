package com.zxw.openapigateway;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zxw.springbootinit.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author MECHREVO
 */
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDubbo
@Component
public class OpenapiGatewayApplication {

    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(OpenapiGatewayApplication.class, args);
        OpenapiGatewayApplication gatewayApplication = context.getBean(OpenapiGatewayApplication.class);
        String result = gatewayApplication.doSayHello("world");
        System.out.println("result: " + result);


        //sentinel限流
        try (Entry entry = SphU.entry("HelloWorld")) {
            // Your business logic here.
            System.out.println("hello world");
        } catch (BlockException e) {
            // Handle rejected request.
            e.printStackTrace();
        }
        // try-with-resources auto exit

    }

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }


}

