package com.zxw.openapiinterface.controller;

import com.zxw.openapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

/**
 * @author zxw
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/get")
    public String getNameByGet(String name, HttpServletRequest request) {
        System.out.println(request.getHeader("zxw"));
        return "GET 你的名字是" + name;
    }

    @RequestMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
//        //从请求头中校验
//        String accessKey = request.getHeader("accessKey");
//        String secretKey = request.getHeader("secretKey");
//        String body = request.getHeader("body");
//        String nonce = request.getHeader("nonce");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        // todo 实际情况是从数据库中查算法已分配给用户
//        if (!accessKey.equals("123")) {
//            return "鉴权失败";
//        }
//        //校验随机数
//        if (Long.parseLong(nonce) > 100000) {
//            return "无权限";
//        }
//        //校验时间戳,todo 时间和当前时间不能超过五分钟
//        if (isWithinFiveMinutes(Long.parseLong(timestamp))) {
//            return "超时处理";
//        }
//        //校验签名, todo 实际从数据库中取出secretKey
//        String genSign = SignUtils.genSign(body, "123");
//        if (!sign.equals(genSign)) {
//            return "签名错误";
//        }
//        todo 统计调用次数加一
        return "GET 你的名字是" + user.getUsername();
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
}