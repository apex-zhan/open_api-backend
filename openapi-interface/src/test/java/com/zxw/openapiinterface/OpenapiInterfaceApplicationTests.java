package com.zxw.openapiinterface;

import com.zxw.openapiclientsdk.client.openApiClient;
import com.zxw.openapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenapiInterfaceApplicationTests {
    @Autowired
    private openApiClient openApiClient;

    @Test
    void contextLoads() {
        String name = openApiClient.getNameByPost("zxw");
        System.out.println(name);
        String name1 = openApiClient.getNameByGet("zxw");
        System.out.println(name1);
        User user = new User();
        user.setUsername("zxw");
        String userNameByPost = openApiClient.getUserNameByPost(user);
        System.out.println(userNameByPost);
    }

}
