package com.zxw.springbootinit.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxw.openapicommon.model.entity.UserInterfaceInfo;
import com.zxw.openapicommon.service.InnerUserInterfaceInfoService;
import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.exception.BusinessException;
import com.zxw.springbootinit.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author MECHREVO
 * 内部用户接口信息服务实现类
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {

    }

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
        return true;
    }

    /**
     * 校验是否还有调用次数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean hasRemainingCalls(long interfaceInfoId, long userId) {
        if (userId <= 0 || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // todo 从缓存中检查调用次数
//        String cacheKey = "remainingCalls:" + userId + ":" + interfaceInfoId;
//        Integer remainingCalls = redisTemplate.opsForValue().get(cacheKey);
//        if (remainingCalls != null) {
//            return remainingCalls > 0;
//        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        queryWrapper.eq("userId", userId);
        queryWrapper.gt("leftNum", 0);

        // 查询数据库，判断是否有剩余调用次数
        long count = userInterfaceInfoService.count(queryWrapper);
        return count > 0;
    }
}
