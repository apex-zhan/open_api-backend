package com.zxw.springbootinit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxw.springbootinit.model.entity.InterfaceInfo;


/**
* @author MECHREVO
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-11-03 14:49:09
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);
}
