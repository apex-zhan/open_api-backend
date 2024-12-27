package com.zxw.springbootinit.model.vo;

import com.zxw.springbootinit.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author MECHREVO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoVO extends InterfaceInfo {

    private Integer totalNum;

}
