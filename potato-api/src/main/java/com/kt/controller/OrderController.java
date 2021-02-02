package com.kt.controller;

import com.kt.bo.SubmitOrderBO;
import com.kt.enums.OrderStatusEnum;
import com.kt.enums.PayMethod;
import com.kt.pojo.OrderStatus;
import com.kt.reps.ApiResult;
import com.kt.service.OrderService;
import com.kt.utils.CookieUtils;
import com.kt.vo.MerChantOrdersVO;
import com.kt.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: potato
 * @description 订单controller层
 * @Author: Tcs
 * @Date: 2020-12-14 09:08
 **/
@Api(value = "订单相关",tags = {"订单相关的api接口"})
@RequestMapping("/orders")
@RestController
public class OrderController extends BaseController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "用户下单",notes = "用户下单",httpMethod = "POST")
    @PostMapping("/create")
    public ApiResult create(@RequestBody SubmitOrderBO submitOrderBO, HttpServletRequest request, HttpServletResponse response){

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return ApiResult.errorMsg("支付方式不支持");
        }

        System.out.println(submitOrderBO.toString());

        // 1.创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();


        // 2.创建订单以后，移除购物车中已结算的商品
        // TODO 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        // CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);
        // 3.向支付中心发送当前订单，用于保存支付中心的数据
        MerChantOrdersVO merChantOrdersVO = orderVO.getMerChantOrdersVO();
        merChantOrdersVO.setReturnUrl(payReturnUrl);
        // 为了方便测试购买，
        merChantOrdersVO.setAmount(1);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","imooc");
        headers.add("password","imooc");
        HttpEntity<MerChantOrdersVO> entity = new HttpEntity<>(merChantOrdersVO,headers);
        ResponseEntity<ApiResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity, ApiResult.class);

        ApiResult apiResult = responseEntity.getBody();
        if (apiResult.getStatus() != 200){
            return ApiResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }
        return ApiResult.ok(orderId);
    }

    @ApiOperation(value = "通知",httpMethod = "POST")
    @PostMapping("notifyMerchantOrderPaid")
    public int notifyMerchantOrderPaid(String merchantOrderId){
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "获取订单状态",notes = "获取订单状态",httpMethod = "GET")
    @GetMapping("getPaidOrderInfo")
    public ApiResult getPaidOrderInfo(String orderId){
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);

        return ApiResult.ok(orderStatus);
    }
}
