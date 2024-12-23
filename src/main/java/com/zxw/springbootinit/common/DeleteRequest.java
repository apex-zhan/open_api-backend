package com.zxw.springbootinit.common;

import java.io.Serializable;
import lombok.Data;

/**
 * 通用的删除请求类
 *
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}