package com.kt.service;

import com.kt.pojo.Carousel;

import java.util.List;

/**
 * @program: potato
 * @description 轮播图service接口
 * @Author: Tan.
 * @Date: 2020-12-05 10:23
 **/
public interface CarouselService {

    /**
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    public List<Carousel> queryAll(Integer isShow);
}
