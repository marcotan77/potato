package com.kt.enums;

/**
 * @program: potato
 * @description 商品评价等级 枚举
 * @author: Tan.
 * @create: 2020-12-05 16:56
 **/
public enum CommentLevel {
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    public final Integer id;
    public final String value;

    CommentLevel(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
