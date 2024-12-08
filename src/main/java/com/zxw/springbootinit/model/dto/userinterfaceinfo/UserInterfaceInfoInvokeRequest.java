package com.zxw.springbootinit.model.dto.userinterfaceinfo;

import lombok.Data;

/**
 * 接口调用请求
 */
@Data
public class UserInterfaceInfoInvokeRequest {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

}
