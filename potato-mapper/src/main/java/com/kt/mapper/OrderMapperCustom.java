package com.kt.mapper;

import com.kt.pojo.OrderStatus;
import com.kt.vo.MyOrderOV;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: Tcs
 * @Date: 2020-12-17 14:03
 **/
public interface OrderMapperCustom {

    public List<MyOrderOV> queryMyOrder(Map<String,Object> map);

    public int getMyOrderStatusCounts(@Param("paramsMap") Map<String, Object> map);

    public List<OrderStatus> getMyOrderTrend(@Param("paramsMap") Map<String, Object> map);
}
