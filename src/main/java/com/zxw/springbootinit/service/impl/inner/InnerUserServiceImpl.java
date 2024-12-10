package com.zxw.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxw.openapicommon.model.entity.User;
import com.zxw.openapicommon.service.InnerUserService;
import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.exception.BusinessException;
import com.zxw.springbootinit.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 内部用户服务类
 *
 * @author MECHREVO
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isEmpty(accessKey)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "非法访问");
        }
        // 根据accessKey和secretKey获取用户信息
        QueryWrapper<User> userqueryWrapper = new QueryWrapper<>();
        userqueryWrapper.eq("access_key", accessKey);
        User user = userMapper.selectOne(userqueryWrapper);
        return user;
    }
}
