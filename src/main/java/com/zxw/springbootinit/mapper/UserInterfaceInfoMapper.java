package com.zxw.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxw.openapicommon.model.entity.InterfaceInfo;
import com.zxw.openapicommon.model.entity.UserInterfaceInfo;

import java.util.List;


/**
* @author MECHREVO
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-11-15 15:15:44
* @Entity UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);
}




