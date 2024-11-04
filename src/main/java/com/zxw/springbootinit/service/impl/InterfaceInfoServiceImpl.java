package com.zxw.springbootinit.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.exception.BusinessException;
import com.zxw.springbootinit.exception.ThrowUtils;
import com.zxw.springbootinit.mapper.InterfaceInfoMapper;
import com.zxw.springbootinit.model.entity.InterfaceInfo;
import com.zxw.springbootinit.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author MECHREVO
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-11-03 14:49:09
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {
    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long infoId = interfaceInfo.getId();
        String infoName = interfaceInfo.getName();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(infoName), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(infoName) && infoName.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

}




