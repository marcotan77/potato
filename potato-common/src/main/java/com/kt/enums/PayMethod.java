package com.kt.enums;

/**
 * @program: potato
 * @description 支付方式 枚举
 * @author: Tcs
 * @create: 2020-12-14 09:18
 **/
public enum PayMethod {

    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");

    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

}
