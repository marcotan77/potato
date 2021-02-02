package com.kt.controller.center;

import com.kt.pojo.Users;
import com.kt.reps.ApiResult;
import com.kt.service.center.CenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: potato
 * @description 用户中心控制层
 * @Author: Tcs
 * @Date: 2020-12-16 10:51
 **/
@Api(value = "center - 用户中心", tags = {"用户中心相关api接口"})
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    @GetMapping("/userInfo")
    public ApiResult userInfo(@ApiParam(name = "userId", value = "用户Id", required = true)
                              @RequestParam String userId) {
        Users users = centerUserService.queryUserInfo(userId);
        return ApiResult.ok(users);
    }


}
