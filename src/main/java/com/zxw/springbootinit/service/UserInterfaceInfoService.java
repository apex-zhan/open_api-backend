package com.zxw.springbootinit.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zxw.springbootinit.model.dto.user.UserInterfaceInfo;


/**
 * @author MECHREVO
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2024-11-15 15:15:44
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userinterfaceInfo, boolean add);

    /**
     * 统计接口调用次数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
