package com.zxw.springbootinit.model.enums;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口状态
 */
public enum InterfaceInfoStatusEnum {
    ONLINE("在线", 1),

    OFFLINE("下线", 0);

    private final String text;

    private final int value;

    InterfaceInfoStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
