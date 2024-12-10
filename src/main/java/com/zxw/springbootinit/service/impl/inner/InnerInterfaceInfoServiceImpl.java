package com.zxw.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.zxw.openapicommon.model.entity.InterfaceInfo;
import com.zxw.openapicommon.service.InnerInterfaceInfoService;
import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.exception.BusinessException;
import com.zxw.springbootinit.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部接口实现类
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
//        从数据库中查询模拟接口是否存在（请求路径，请求方法，请求参数，返回接口信息，为空表示不存在）
        if (StringUtils.isAllBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求路径和请求方法不能为空");
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url).eq("method", method);
        InterfaceInfo interfaceInfo = interfaceInfoMapper.selectOne(queryWrapper);
        return interfaceInfo;
    }
}
