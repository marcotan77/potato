package com.kt.controller;

import com.kt.bo.AddressBO;
import com.kt.pojo.UserAddress;
import com.kt.reps.ApiResult;
import com.kt.service.AddressService;
import com.kt.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: potato
 * @description
 * @Author: Tcs
 * @Date: 2020-12-12 11:05
 **/
@Api(value = "地址相关", tags = {"地址相关api接口"})
@RequestMapping("address")
@RestController
public class AddressController {

    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作
     * 1.查询用户的所有收货地址列表
     * 2.新增收货地址
     * 3.删除收货地址
     * 4.修改收货地址
     * 5.设置默认地址
     */

    @Autowired
    private AddressService addressService;

    @ApiOperation(value = "根据用户Id查询收货地址列表",notes = "根据用户Id查询收货地址列表" ,httpMethod = "POST")
    @PostMapping("/list")
    public ApiResult list(
            @RequestParam String userId
    ){
        if (StringUtils.isBlank(userId)){
            return ApiResult.errorMsg("");
        }
        List<UserAddress> list =  addressService.queryAll(userId);
        return ApiResult.ok(list);
    }

    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    @PostMapping("/add")
    public ApiResult add(@RequestBody AddressBO addressBO) {

        ApiResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.addNewUserAddress(addressBO);

        return ApiResult.ok();
    }
    private ApiResult checkAddress(AddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return ApiResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return ApiResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return ApiResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return ApiResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return ApiResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return ApiResult.errorMsg("收货地址信息不能为空");
        }

        return ApiResult.ok();
    }

    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    @PostMapping("/update")
    public ApiResult update(@RequestBody AddressBO addressBO) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return ApiResult.errorMsg("修改地址错误：addressId不能为空");
        }

        ApiResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.updateUserAddress(addressBO);

        return ApiResult.ok();
    }

    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    @PostMapping("/delete")
    public ApiResult delete(
            @RequestParam String userId,
            @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ApiResult.errorMsg("");
        }

        addressService.deleteUserAddress(userId, addressId);
        return ApiResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    @PostMapping("/setDefalut")
    public ApiResult setDefalut(
            @RequestParam String userId,
            @RequestParam String addressId) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return ApiResult.errorMsg("");
        }

        addressService.updateUserAddressToBeDefault(userId, addressId);
        return ApiResult.ok();
    }
}
