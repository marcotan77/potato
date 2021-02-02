package com.kt.service.center;

import com.kt.pojo.Orders;
import com.kt.utils.ResponsePagination;
import com.kt.vo.OrderStatusCountsVO;

/**
 * @Author: Tcs
 * @Date: 2020-12-17 14:32
 **/
public interface MyOrderService {

    /**
     * 查询我的订单列表
     * @param userId
     * @param orderStatus
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ResponsePagination queryMyOrders(String userId,Integer orderStatus,Integer pageIndex,Integer pageSize);

    /**
     * 订单状态 --> 商家发货
     * @param orderId
     */
    public void updateDeliverOrderStatus(String orderId);

    /**
     * 查询我的订单
     * @param userId
     * @param orderId
     * @return
     */
    public Orders queryMyOrder(String userId,String orderId);

    /**
     * 更新订单状态 --> 确认收货
     * @param orderId
     * @return
     */
    public boolean updateReceiveOrderStatus(String orderId);

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    public boolean deleteOrder(String orderId,String userId);

    /**
     * 查询用户订单数
     * @param userId
     */
    public OrderStatusCountsVO getOrderStatusCounts(String userId);

    /**
     * 获得分页的订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public ResponsePagination getOrdersTrend(String userId,
                                          Integer page,
                                          Integer pageSize);
}
