package com.kt.controller.center;

import com.kt.controller.BaseController;
import com.kt.pojo.Orders;
import com.kt.reps.ApiResult;
import com.kt.service.center.MyOrderService;
import com.kt.utils.ResponsePagination;
import com.kt.vo.OrderStatusCountsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * 用户中心订单控制层
 *
 * @Author: Tcs
 * @Date: 2020-12-17 14:58
 **/

@Api(value = "用户中心我的订单", tags = {"用户中心我的订单相关接口"})
@RestController
@RequestMapping("myorders")
public class MyOrderController extends BaseController {

    @Autowired
    private MyOrderService myOrderService;

    @ApiOperation(value = "获得订单状态数概况", notes = "获得订单状态数概况", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public ApiResult statusCounts(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return ApiResult.errorMsg(null);
        }

        OrderStatusCountsVO result = myOrderService.getOrderStatusCounts(userId);

        return ApiResult.ok(result);
    }

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public ApiResult query(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize
    ) {
        if (StringUtils.isBlank(userId)) {
            return ApiResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        ResponsePagination responsePagination = myOrderService.queryMyOrders(userId, orderStatus, page, pageSize);
        return ApiResult.ok(responsePagination);
    }

    @ApiOperation(value = "商家发货",notes = "商家发货",httpMethod = "GET")
    @GetMapping("/deliver")
    public ApiResult deliver(
            @ApiParam(name = "orderId", value = "订单Id", required = true)
            @RequestParam String orderId) throws Exception{
        myOrderService.updateDeliverOrderStatus(orderId);
        return ApiResult.ok();
    }

    @ApiOperation(value = "用户确认收货",notes = "用户确认收货",httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public ApiResult confirmReceive(
            @ApiParam(name = "orderId", value = "订单Id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId) throws Exception{
        ApiResult checkResult = checkUserOrder(userId,orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }
        boolean res = myOrderService.updateReceiveOrderStatus(orderId);
        if (!res){
            return ApiResult.errorMsg("订单确认收货失败");
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

    @ApiOperation(value = "用户删除订单",notes = "用户删除订单",httpMethod = "POST")
    @PostMapping("/delete")
    public ApiResult delete(
            @ApiParam(name = "orderId", value = "订单Id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId) throws Exception{
        ApiResult checkResult = checkUserOrder(userId,orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()){
            return checkResult;
        }
        boolean res = myOrderService.deleteOrder(orderId, userId);
        if (!res){
            return ApiResult.errorMsg("订单删除失败");
        }
        return ApiResult.ok();
    }


    @ApiOperation(value = "查询订单动向", notes = "查询订单动向", httpMethod = "POST")
    @PostMapping("/trend")
    public ApiResult trend(
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

        ResponsePagination grid = myOrderService.getOrdersTrend(userId,
                page,
                pageSize);

        return ApiResult.ok(grid);
    }
}
