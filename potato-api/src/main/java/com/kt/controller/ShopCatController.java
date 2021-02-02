package com.kt.controller;

import com.kt.bo.ShopcartBO;
import com.kt.reps.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: potato
 * @description 购物车接口controller
 * @Author: Tan.
 * @Date: 2020-12-11 10:49
 **/
@Api(value = "购物车接口Controller",tags = {"购物车接口相关的api"})
@RequestMapping("shopcart")
@RestController
public class ShopCatController {
    public final static Logger logger = LoggerFactory.getLogger(ShopCatController.class);

    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("/add")
    public ApiResult add(
            @RequestParam String userId,
            @RequestBody ShopcartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response
            ){
        if (StringUtils.isBlank(userId)){
            return ApiResult.errorMsg("");
        }
        logger.info(shopcartBO.toString());
        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        return ApiResult.ok();
    }

    @ApiOperation(value = "从购物车删除商品",notes = "从购物车删除商品",httpMethod = "POST")
    @PostMapping("/del")
    public ApiResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)){
            return ApiResult.errorMsg("参数不能为空");
        }

        //TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车的商品数据

        return  ApiResult.ok();
    }

}
