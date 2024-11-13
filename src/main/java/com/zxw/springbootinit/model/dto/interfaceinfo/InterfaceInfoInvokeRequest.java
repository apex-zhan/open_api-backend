package com.zxw.springbootinit.model.dto.interfaceinfo;

import lombok.Data;

/**
 * 接口调用请求
 */
@Data
public class InterfaceInfoInvokeRequest {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

}
