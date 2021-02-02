package com.kt.mapper;

import com.kt.vo.ItemCommentVO;
import com.kt.vo.SearchItemsVO;
import com.kt.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @program: potato
 * @description
 * @Author: Tan.
 * @Date: 2020-12-05 17:24
 **/
public interface ItemsCustomMapper {

    public List<ItemCommentVO> queryItemComments(Map<String,Object> map);

    public List<SearchItemsVO> searchItems(Map<String,Object> map);

    public List<SearchItemsVO> searchItemsByThirdCat(Map<String,Object> map);

    public List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIds);

    public int decreaseItemSpecStock(@Param("specId") String specId,@Param("pendingCounts") Integer pendingCounts);
}
