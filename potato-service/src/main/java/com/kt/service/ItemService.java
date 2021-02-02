package com.kt.service;

import com.kt.pojo.Items;
import com.kt.pojo.ItemsImg;
import com.kt.pojo.ItemsParam;
import com.kt.pojo.ItemsSpec;
import com.kt.utils.ResponsePagination;
import com.kt.vo.CommentLevelCountVO;
import com.kt.vo.ShopcartVO;


import java.util.List;

/**
 * @program: potato
 * @description
 * @Author: Tan.
 * @Date: 2020-12-05 15:46
 **/
public interface ItemService {

    /**
     * 根据商品ID查询详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品ID查询商品图片列表
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品Id查询商品规格
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品ID查询商品参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品Id查询商品的评价等级数量
     * @param itemId
     * @return
     */
    CommentLevelCountVO queryCommentCounts(String itemId);

    /**
     * 根据商品ID查询商品评价（分页）
     * @param itemId
     * @param Level
     * @return
     */
    public ResponsePagination queryPageComment(String itemId, Integer Level, Integer page, Integer pageSize);

    /**
     * 搜索商品列表
     * @param itemName
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public ResponsePagination searchItems(String itemName, String sort, Integer page, Integer pageSize);
    /**
     * 根据分类ID搜索商品列表
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    public ResponsePagination searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格Ids查询最新的购物车中商品数据（用于刷新渲染购物车里的商品信息）
     * @param specIds
     * @return
     */
    public List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据规格id查询具体规格信息
     * @param specId
     * @return
     */
    public ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品Id获取商品主图片
     * @param itemId
     * @return
     */
    public String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCount
     */
    public void decreaseItemSpecStock(String specId,int buyCount);
 }
