package com.zxw.springbootinit.model.dto.userinterfaceinfo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口信息
 *
 * @TableName interface_info
 */
@TableName(value = "user_interface_info")
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {
    /**
     * 主键
     */

    private Long id;

    /**
     * 调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;


    /**
     * 接口状态（0-关闭，1-开启）
     */
    private Integer status;


    private static final long serialVersionUID = 1L;
}
