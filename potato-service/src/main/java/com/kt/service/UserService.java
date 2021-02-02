package com.kt.service;

import com.kt.bo.UserBO;
import com.kt.pojo.Users;

/**
 * @program: potato
 * @description 用户service接口层
 * @Author: Tan.
 * @Date: 2020-12-03 15:20
 **/
public interface UserService {

    /**
     * 判断用户名是否存在
     * @param userName
     * @return boolean
     */
    public boolean queryUserNameIsExist(String userName);

    /**
     * 创建用户
     * @param userBO
     * @return Users
     */
    public Users createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配，用于登录
     * @param username
     * @param password
     * @return
     */
    public Users queryUserForLogin(String username, String password);
}
