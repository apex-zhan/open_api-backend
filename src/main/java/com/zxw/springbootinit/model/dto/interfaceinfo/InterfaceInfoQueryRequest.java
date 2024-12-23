package com.zxw.springbootinit.model.dto.interfaceinfo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zxw.springbootinit.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询接口信息请求
 */
@TableName(value = "interface_info")
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 请求参数,格式为json
     * [
     *   {"name": "username","type": "string"}
     * ]
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 创建人
     */
    private Long userId;


    private static final long serialVersionUID = 1L;

}
