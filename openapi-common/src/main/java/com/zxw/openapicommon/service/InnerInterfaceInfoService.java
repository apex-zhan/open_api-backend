package com.zxw.openapicommon.service;

import com.zxw.openapicommon.model.entity.InterfaceInfo;


/**
 * @author zxw
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2024-11-03 14:49:09
 */
public interface InnerInterfaceInfoService{

    /**
     * 从数据库中查询模拟接口是否存在
     *
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

}
