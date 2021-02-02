package com.kt.enums;

/**
 * @program: potato
 * @description 性别 枚举
 * @author: Tan.
 * @create: 2020-12-03 16:42
 **/
public enum Sex {
    WOMAN(0, "女"),

    MAN(1,"男"),

    SECRET(2,"保密");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
