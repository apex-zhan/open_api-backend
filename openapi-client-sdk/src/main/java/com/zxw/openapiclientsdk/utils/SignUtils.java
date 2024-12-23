package com.zxw.openapiclientsdk.utils;


import cn.hutool.crypto.digest.DigestUtil;

/**
 * 签名工具
 * @author MECHREVO
 */

public class SignUtils {
    public static String genSign(String body, String secretKey) {
        String content = body + "." + secretKey;
        return DigestUtil.md5Hex(content);
    }
}
