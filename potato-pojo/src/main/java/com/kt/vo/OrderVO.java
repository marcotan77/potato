package com.kt.vo;

/**
 * @program: potato
 * @description
 * @Author: Tcs
 * @Date: 2020-12-15 13:54
 **/
public class OrderVO {
    private String orderId;
    private MerChantOrdersVO merChantOrdersVO;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerChantOrdersVO getMerChantOrdersVO() {
        return merChantOrdersVO;
    }

    public void setMerChantOrdersVO(MerChantOrdersVO merChantOrdersVO) {
        this.merChantOrdersVO = merChantOrdersVO;
    }
}
