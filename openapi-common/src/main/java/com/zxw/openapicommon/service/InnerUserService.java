package com.zxw.openapicommon.service;

import com.zxw.openapicommon.model.entity.User;
/**
 * 用户服务
 */
public interface InnerUserService {

    /**
     * 从数据库中取ak，sk
     *
     * @param accessKey
     * @return
     */
    User getInvokeUser(String accessKey);

}
