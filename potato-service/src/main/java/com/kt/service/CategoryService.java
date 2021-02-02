package com.kt.service;

import com.kt.pojo.Category;
import com.kt.vo.CategoryVO;
import com.kt.vo.NewItemsVO;

import java.util.List;

/**
 * @program: potato
 * @description 分类service接口
 * @Author: Tan.
 * @Date: 2020-12-05 10:57
 **/
public interface CategoryService {

    /**
     * 查询所有一级分类
     * @return
     */
    public List<Category> queryRootLevelCat();

    /**
     * 根据一级分类ID查询子分类
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询首页下的每个一级分类下的6条最新商品数据
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNemItemLazy(Integer rootCatId);
}
