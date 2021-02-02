package com.kt.service.center;

import com.kt.bo.center.CenterUserBO;
import com.kt.pojo.Users;

/**
 * @program: potato
 * @description
 * @Author: Tcs
 * @Date: 2020-12-16 10:53
 **/
public interface CenterUserService {

    /**
     * 根据用户Id查询用户信息
     *
     * @param userId
     * @return
     */
    public Users queryUserInfo(String userId);


    /**
     * 修改用户信息
     *
     * @param userId
     * @param centerUserBO
     */
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    /**
     * 用户头像更新
     *
     * @param userId
     * @param faceUrl
     * @return
     */
    public Users updateUserFace(String userId, String faceUrl);
}
