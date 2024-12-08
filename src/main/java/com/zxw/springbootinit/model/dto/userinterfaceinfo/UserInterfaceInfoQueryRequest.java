package com.zxw.springbootinit.model.dto.userinterfaceinfo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zxw.springbootinit.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询接口信息请求
 */
@TableName(value = "user_interface_info")
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 接口id
     */
    private String interfaceInfoId;

    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;

    /**
     * 调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;




    private static final long serialVersionUID = 1L;

}
