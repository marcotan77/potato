package com.kt.service.impl;

import com.kt.mapper.CategoryCustomMapper;
import com.kt.mapper.CategoryMapper;
import com.kt.pojo.Category;
import com.kt.service.CategoryService;
import com.kt.vo.CategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: potato
 * @description
 * @Author: Tan.
 * @Date: 2020-12-05 10:59
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryCustomMapper categoryCustomMapper;

    /**
     * 查询所有一级分类
     *
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type",1);
        List<Category> categoryList = categoryMapper.selectByExample(example);
        return categoryList;
    }

    /**
     * 根据一级分类ID查询子分类
     *
     * @param rootCatId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryCustomMapper.getSubCatList(rootCatId);
    }

    /**
     * 查询首页下的每个一级分类下的6条最新商品数据
     *
     * @param rootCatId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List getSixNemItemLazy(Integer rootCatId) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("rootCatId",rootCatId);
        return categoryCustomMapper.getSixNemItemLazy(map);
    }
}
