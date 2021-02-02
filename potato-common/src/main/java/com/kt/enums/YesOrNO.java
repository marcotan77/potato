package com.kt.enums;

/**
 * @program: potato
 * @description 是否 枚举
 * @author: Tan.
 * @create: 2020-12-05 10:32
 **/
public enum YesOrNO {
    NO(0, "否"),
    YES(1, "是");

    public final Integer type;
    public final String value;

    YesOrNO(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
