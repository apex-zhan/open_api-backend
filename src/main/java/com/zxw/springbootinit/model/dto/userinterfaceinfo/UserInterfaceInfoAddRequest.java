package com.zxw.springbootinit.model.dto.userinterfaceinfo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 请求参数对象
 *
 * @TableName interface_info
 */
@TableName(value = "user_interface_info")
@Data
public class UserInterfaceInfoAddRequest implements Serializable {
    /**
     * 调用者id
     */
    private Long userId;

    /**
     * 接口id
     */
    private String interfaceInfoId;

    /**
     * 调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}