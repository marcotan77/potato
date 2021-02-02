package com.kt.service;

import com.kt.bo.SubmitOrderBO;
import com.kt.pojo.OrderStatus;
import com.kt.vo.OrderVO;

/**
 * @program: potato
 * @description
 * @Author: Tcs
 * @Date: 2020-12-14 09:23
 **/
public interface OrderService {
    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     */
     public OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
     public void updateOrderStatus(String orderId,Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
     public OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder();
}
