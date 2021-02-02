package com.kt.controller;

import java.io.File;

/**
 * @program: potato
 * @description 基础controller
 * @Author: Tan.
 * @Date: 2020-12-08 15:20
 **/
public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";

    public static final Integer DEFAULT_PAGE_SIZE = 10;

    // 支付中心
    String paymentUrl = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    String payReturnUrl = "http://192.168.110.126:8088/potato-api/orders/notifyMerchanOrderPaid";

//    String IMAGE_USER_FACE_LOCATION = "\\tan\\img ";
    public static final  String IMAGE_USER_FACE_LOCATION = File.separator+"tan"+File.separator+"img";
}
