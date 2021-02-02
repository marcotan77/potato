package com.kt.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kt.enums.OrderStatusEnum;
import com.kt.enums.YesOrNO;
import com.kt.mapper.OrderMapperCustom;
import com.kt.mapper.OrderStatusMapper;
import com.kt.mapper.OrdersMapper;
import com.kt.pojo.OrderStatus;
import com.kt.pojo.Orders;
import com.kt.service.center.MyOrderService;
import com.kt.utils.ResponsePagination;
import com.kt.vo.MyOrderOV;
import com.kt.vo.OrderStatusCountsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Tcs
 * @Date: 2020-12-17 14:35
 **/
@Service
public class MyOrderServiceImpl implements MyOrderService {

    @Autowired
    private OrderMapperCustom orderMapperCustom;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private OrdersMapper ordersMapper;
    /**
     * 查询我的订单列表
     *
     * @param userId
     * @param orderStatus
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponsePagination queryMyOrders(String userId, Integer orderStatus, Integer pageIndex, Integer pageSize) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId",userId);
        if (orderStatus != null){
            map.put("orderStatus",orderStatus);
        }
        PageHelper.startPage(pageIndex,pageSize);
        List<MyOrderOV> list = orderMapperCustom.queryMyOrder(map);
        return setPage(list, pageIndex);
    }

    public ResponsePagination setPage(List<?> list, Integer pageIndex) {
        PageInfo<?> pageList = new PageInfo<>(list);
        ResponsePagination page = new ResponsePagination();
        page.setPage(pageIndex);
        page.setRows(list);
        page.setTotal(pageList.getPages());
        page.setRecords(pageList.getTotal());
        return page;
    }

    /**
     * 订单状态 --> 商家发货
     *
     * @param orderId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus updateStatus = new OrderStatus();
        updateStatus.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateStatus.setDeliverTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        criteria.andEqualTo("orderStatus",OrderStatusEnum.WAIT_DELIVER.type);

        orderStatusMapper.updateByExampleSelective(updateStatus,example);
    }

    /**
     * 查询我的订单
     *
     * @param userId
     * @param orderId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setUserId(userId);
        orders.setIsDelete(YesOrNO.NO.type);

        return ordersMapper.selectOne(orders);
    }

    /**
     * 更新订单状态 --> 确认收货
     *
     * @param orderId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        orderStatus.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orderId",orderId);
        criteria.andEqualTo("orderStatus",OrderStatusEnum.WAIT_RECEIVE.type);

        int result = orderStatusMapper.updateByExampleSelective(orderStatus, example);

        return result == 1 ? true : false;
    }

    /**
     * 删除订单
     *
     * @param orderId
     * @param userId
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String orderId, String userId) {
        Orders orders = new Orders();
        orders.setIsDelete(YesOrNO.YES.type);
        orders.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",orderId);
        criteria.andEqualTo("userId",userId);

        int result = ordersMapper.updateByExampleSelective(orders, example);
        return result == 1 ? true : false;
    }

    /**
     * 查询用户订单数
     *
     * @param userId
     */
    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getOrderStatusCounts(String userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        map.put("orderStatus", OrderStatusEnum.WAIT_PAY.type);
        int waitPayCounts = orderMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_DELIVER.type);
        int waitDeliverCounts = orderMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.WAIT_RECEIVE.type);
        int waitReceiveCounts = orderMapperCustom.getMyOrderStatusCounts(map);

        map.put("orderStatus", OrderStatusEnum.SUCCESS.type);
        map.put("isComment", YesOrNO.NO.type);
        int waitCommentCounts = orderMapperCustom.getMyOrderStatusCounts(map);

        OrderStatusCountsVO countsVO = new OrderStatusCountsVO(waitPayCounts,
                waitDeliverCounts,
                waitReceiveCounts,
                waitCommentCounts);
        return countsVO;
    }

    /**
     * 获得分页的订单动向
     *
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    @Transactional(propagation=Propagation.SUPPORTS)
    @Override
    public ResponsePagination getOrdersTrend(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        PageHelper.startPage(page, pageSize);
        List<OrderStatus> list = orderMapperCustom.getMyOrderTrend(map);

        return setPage(list, page);
    }
}
