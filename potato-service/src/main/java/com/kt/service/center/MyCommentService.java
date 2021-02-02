package com.kt.service.center;

import com.kt.bo.center.OrderItemsCommentBO;
import com.kt.pojo.OrderItems;
import com.kt.utils.ResponsePagination;

import java.util.List;

/**
 * @Author: Tcs
 * @Date: 2020-12-18 10:32
 **/
public interface MyCommentService {
    /**
     * 根据订单Id查询关联的商品
     * @param orderId
     * @return
     */
    public List<OrderItems> queryPendingComment(String orderId);

    /**
     * 保存用户的评论
     * @param orderId
     * @param userId
     * @param commentList
     */
    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);


    /**
     * 我的评价查询 分页
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public ResponsePagination queryMyComments(String userId, Integer page, Integer pageSize);
}
