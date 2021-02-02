package com.kt.mapper;

import com.kt.vo.CategoryVO;
import com.kt.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryCustomMapper{

    public List<CategoryVO> getSubCatList(Integer rootCatId);

    public List<NewItemsVO> getSixNemItemLazy(@Param("paramsMap") Map<String,Object> map);
}