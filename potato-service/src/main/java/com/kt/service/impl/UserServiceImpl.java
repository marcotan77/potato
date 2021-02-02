package com.kt.service.impl;

import com.kt.bo.UserBO;
import com.kt.enums.Sex;
import com.kt.mapper.UsersMapper;
import com.kt.pojo.Users;
import com.kt.service.UserService;
import com.kt.utils.DateUtil;
import com.kt.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;


/**
 * @program: potato
 * @description 用户service实现层
 * @Author: Tan.
 * @Date: 2020-12-03 15:22
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    private static final String FACE = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1606997104209&di=63b704fcb860fea46d9610b9c33e2f33&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Felement_origin_min_pic%2F16%2F09%2F19%2F1157df5f75d1b12.jpg";

    /**
     * 判断用户名是否存在
     *
     * @param userName
     * @return boolean
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String userName) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", userName);
        Users result = usersMapper.selectOneByExample(userExample);
        return result == null ? false : true;
    }

    /**
     * 创建用户
     *
     * @param userBO
     * @return Users
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        String userId = sid.nextShort();
        Users user = new Users();
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 默认用户昵称同用户名
        user.setNickname(userBO.getUsername());
        // 默认头像
        user.setFace(FACE);
        // 默认生日
        user.setBirthday(DateUtil.stringToDate("2020-01-01"));
        user.setSex(Sex.SECRET.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        usersMapper.insert(user);
        return user;
    }

    /**
     * 检索用户名和密码是否匹配，用于登录
     *
     * @param username
     * @param password
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username", username);
        userCriteria.andEqualTo("password", password);
        Users result = usersMapper.selectOneByExample(userExample);
        return result;
    }
}
