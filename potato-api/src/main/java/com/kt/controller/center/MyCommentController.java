package com.kt.controller.center;

import com.kt.bo.center.OrderItemsCommentBO;
import com.kt.controller.BaseController;
import com.kt.enums.YesOrNO;
import com.kt.pojo.Orders;
import com.kt.reps.ApiResult;
import com.kt.service.center.MyCommentService;
import com.kt.service.center.MyOrderService;
import com.kt.utils.ResponsePagination;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Tcs
 * @Date: 2020-12-18 10:37
 **/
@Api(value = "用户中心评价模块", tags = {"用户中心评价模块相关接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentController extends BaseController {

    @Autowired
    private MyCommentService myCommentService;

    @Autowired
    private MyOrderService myOrderService;

    @ApiOperation(value = "查询评价", notes = "查询评价", httpMethod = "POST")
    @PostMapping("/pending")
    public ApiResult pending(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单Id", required = true)
            @RequestParam String orderId) {
        ApiResult checkResult = checkUserOrder(userId,orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }
        Orders myOrder = (Orders) checkResult.getData();
        if (myOrder.getIsComment() == YesOrNO.YES.type){
            return ApiResult.errorMsg("该笔订单已经评价");
        }
        return ApiResult.ok();
    }

    /**
     * 用户验证用户和订单是否有关联，避免非法用户调用
     * @param userId
     * @param orderId
     * @return
     */
    private ApiResult checkUserOrder(String userId,String orderId){
        Orders orders = myOrderService.queryMyOrder(userId, orderId);
        if (orders == null){
            return ApiResult.errorMsg("订单不存在");
        }
        return ApiResult.ok();
    }
    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public ApiResult saveList(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {

        System.out.println(commentList);

        // 判断用户和订单是否关联
        ApiResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }
        // 判断评论内容list不能为空
        if (commentList == null || commentList.isEmpty() || commentList.size() == 0) {
            return ApiResult.errorMsg("评论内容不能为空！");
        }

        myCommentService.saveComments(orderId, userId, commentList);
        return ApiResult.ok();
    }

    @ApiOperation(value = "查询我的评价", notes = "查询我的评价", httpMethod = "POST")
    @PostMapping("/query")
    public ApiResult query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return ApiResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        ResponsePagination grid = myCommentService.queryMyComments(userId,
                page,
                pageSize);

        return ApiResult.ok(grid);
    }
}
