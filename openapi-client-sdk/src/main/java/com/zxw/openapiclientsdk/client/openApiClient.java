package com.zxw.openapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.zxw.openapiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.zxw.openapiclientsdk.utils.SignUtils.genSign;

/**
 * @author zxw
 */

public class openApiClient {


    private final String accessKey;
    private final String secretKey;

    private static final String GATEWAY_HOST = "http://127.0.0.1:8090";

    public openApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getNameByPost(String name) {
        //POST请求
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result1 = HttpUtil.post(GATEWAY_HOST + "/api/name/", paramMap);
        return result1;
    }

    public String getNameByGet(String name) {
        //GET请求
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result2 = HttpUtil.get(GATEWAY_HOST + "/api/name/", paramMap);
        return result2;
    }


    /**
     * 请求头
     *
     * @return
     */
    public Map<String, String> getHeaderMap(String body) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("nonce", RandomUtil.randomNumbers(5));
        headerMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        headerMap.put("accessKey", accessKey);
        headerMap.put("body", body);
        //一定不能放到请求头中
        //headerMap.put("secretKey", secretKey);
        headerMap.put("sign", genSign(body, secretKey));
        return headerMap;
    }


    /**
     * 创建 HTTP POST 请求
     *
     * @param user
     * @return
     */
    public String getUserNameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);
        String url = GATEWAY_HOST + "/api/name/user";
        HttpResponse httpResponse = HttpRequest.post(url)
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        return httpResponse.body();
    }
}
