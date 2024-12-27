package com.zxw.springbootinit.controller;


import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zxw.openapicommon.model.entity.InterfaceInfo;
import com.zxw.openapicommon.model.entity.UserInterfaceInfo;
import com.zxw.springbootinit.common.BaseResponse;
import com.zxw.springbootinit.common.ErrorCode;
import com.zxw.springbootinit.common.ResultUtils;
import com.zxw.springbootinit.exception.BusinessException;
import com.zxw.springbootinit.mapper.UserInterfaceInfoMapper;
import com.zxw.springbootinit.model.vo.InterfaceInfoVO;
import com.zxw.springbootinit.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author MECHREVO
 * 分析控制器
 */
@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {
    @Resource
    private InterfaceInfoService interfaceInfoService;
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    /**
     * 查询调用次数最多的3个接口
     * @return
     */
    @GetMapping("/top/interface/invoke")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {
        List<com.zxw.openapicommon.model.entity.UserInterfaceInfo> listTopInvokeInterfaceInfo = userInterfaceInfoMapper.listTopInvokeInterfaceInfo(3);
//        {1 -> obj1, 2 -> obj2, 3 -> obj3}
        Map<Long, List<UserInterfaceInfo>> interfaceInfoIdObjMap = listTopInvokeInterfaceInfo.stream().collect(Collectors.groupingBy(UserInterfaceInfo::getInterfaceInfoId));
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", interfaceInfoIdObjMap.keySet());
        List<InterfaceInfo> list = interfaceInfoService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //list转成listVO,只看见totalNum，map就是把一个类型映射成另一个类型
        List<InterfaceInfoVO> interfaceInfoVOList = list.stream().map(interfaceInfo -> {
            InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
            BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);
            //获取每个接口的调用次数
            int totalNum = interfaceInfoIdObjMap.get(interfaceInfo.getId()).get(0).getTotalNum();
            interfaceInfoVO.setTotalNum(totalNum);
            return interfaceInfoVO;
        }).collect(Collectors.toList());
        return ResultUtils.success(interfaceInfoVOList);
    }

}
