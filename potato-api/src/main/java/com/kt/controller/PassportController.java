package com.kt.controller;

import com.kt.bo.UserBO;
import com.kt.pojo.Users;
import com.kt.reps.ApiResult;
import com.kt.service.UserService;
import com.kt.utils.CookieUtils;
import com.kt.utils.JsonUtils;
import com.kt.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: potato
 * @description
 * @author: Tan.
 * @create: 2020-11-11 17:11
 **/
@Api(value = "注册登录", tags = "用于注册登录的相关接口")
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public ApiResult userNameIsExist(@RequestParam String username) {
        // 判断用户名是否为空
        if (StringUtils.isBlank(username)) {
            return ApiResult.errorMsg("用户名不能为空");
        }
        // 判断用户是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist) {
            return ApiResult.errorMsg("用户名已经存在");
        }
        return ApiResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public ApiResult regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPWD = userBO.getConfirmPassword();
        // 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPWD)) {
            return ApiResult.errorMsg("用户名或密码不能为空");
        }
        // 判断用户是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist) {
            return ApiResult.errorMsg("用户名已经存在");
        }
        // 密码长度不能少于6位
        if (password.length() < 6) {
            return ApiResult.errorMsg("密码长度不能少于6位");
        }
        // 判断两次密码是否一致
        if (!password.equals(confirmPWD)) {
            return ApiResult.errorMsg("两次密码输入不一致");
        }
        // 实现注册
        Users user = userService.createUser(userBO);
        user = setNUllProperty(user);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(user), true);
        // TODO 生成用户token ，存入redis会话
        // TODO 同步购物车数据
        return ApiResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public ApiResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        // 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return ApiResult.errorMsg("用户名或密码不能为空");
        }
        Users users = null;
        try {
            users = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
            if (null == users) {
                return ApiResult.errorMsg("用户名或密码不正确");
            }
            users = setNUllProperty(users);

            CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);

            // TODO 生成用户token ，存入redis会话
            // TODO 同步购物车数据
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResult.ok(users);
    }

    private Users setNUllProperty(Users usersResult) {
        usersResult.setPassword(null);
        usersResult.setRealname(null);
        usersResult.setBirthday(null);
        usersResult.setCreatedTime(null);
        usersResult.setUpdatedTime(null);
        return usersResult;
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public ApiResult logout(@RequestParam String userId,HttpServletRequest request ,HttpServletResponse response) {

        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request,response,"user");

        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式回话中需要清除用户数据

        return ApiResult.ok();
    }

}
