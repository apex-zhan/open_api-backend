package com.zxw.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxw.openapicommon.model.entity.UserInterfaceInfo;
import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.exception.BusinessException;
import com.zxw.springbootinit.mapper.UserInterfaceInfoMapper;
import com.zxw.springbootinit.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用户接口信息服务实现类
 *
 * @author MECHREVO
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2024-11-15 15:15:44
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {
    private final ConcurrentHashMap<Long, Object> lockMap = new ConcurrentHashMap<>();

    /**
     * 校验用户调用接口关系参数是否合法
     *
     * @param userinterfaceInfo
     * @param add
     */
    @Override
    public void validUserInterfaceInfo(com.zxw.openapicommon.model.entity.UserInterfaceInfo userinterfaceInfo, boolean add) {
        if (userinterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //获取调用者id
        Long userId = userinterfaceInfo.getUserId();
        //创建时所有参数不能为空
        if (add) {
            if (userId == null || userinterfaceInfo.getInterfaceInfoId() == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
            if (userinterfaceInfo.getLeftNum() < 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余调用次数不能小于0");
            }
        }
    }

    /**
     * 调用次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ReentrantLock lock = (ReentrantLock) lockMap.computeIfAbsent(interfaceInfoId, key -> new ReentrantLock());
        try {
            if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
                UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("interfaceInfoId", interfaceInfoId);
                updateWrapper.eq("userId", userId);
                updateWrapper.gt("leftNum", 0);
                updateWrapper.setSql("leftNum = leftNum - 1,totalNum = totalNum + 1");
                return this.update(updateWrapper);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return false;
    }

    /**
     * todo 校验是否有剩余次数
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
//    @Override
//    public boolean hasRemainingCalls(long interfaceInfoId, long userId) {
//        if (userId <= 0 || interfaceInfoId <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
//        queryWrapper.eq("userId", userId);
//        queryWrapper.gt("leftNum", 0);
//
//        // 查询数据库，判断是否有剩余调用次数
//        long count = this.count(queryWrapper);
//        return count > 0;
//    }

}







