package com.zxw.openapicommon.service;

import com.zxw.openapicommon.model.entity.UserInterfaceInfo;

/**
 * @author MECHREVO
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2024-11-15 15:15:44
 */
public interface InnerUserInterfaceInfoService {
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 统计接口调用次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 校验是否有剩余次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean hasRemainingCalls(long interfaceInfoId, long userId);
}
