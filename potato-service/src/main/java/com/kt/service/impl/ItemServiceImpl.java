package com.kt.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kt.enums.CommentLevel;
import com.kt.enums.YesOrNO;
import com.kt.mapper.*;
import com.kt.pojo.*;
import com.kt.service.ItemService;
import com.kt.utils.DesensitizationUtil;
import com.kt.utils.ResponsePagination;
import com.kt.vo.CommentLevelCountVO;
import com.kt.vo.ItemCommentVO;
import com.kt.vo.SearchItemsVO;
import com.kt.vo.ShopcartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @program: potato
 * @description
 * @Author: Tan.
 * @Date: 2020-12-05 15:46
 **/
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsCustomMapper itemsCustomMapper;

    /**
     * 根据商品ID查询详情
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 根据商品ID查询商品图片列表
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    /**
     * 根据商品Id查询商品规格
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    /**
     * 根据商品ID查询商品参数
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    /**
     * 根据商品Id查询商品的评价等级数量
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountVO queryCommentCounts(String itemId) {

        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.id);

        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.id);

        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.id);

        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountVO countVO = new CommentLevelCountVO();
        countVO.setGoodCounts(goodCounts);
        countVO.setNormalCounts(normalCounts);
        countVO.setBadCounts(badCounts);
        countVO.setTotalCounts(totalCounts);
        return countVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId, Integer level) {

        ItemsComments comments = new ItemsComments();
        comments.setItemId(itemId);
        if (level != null) {
            comments.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(comments);
    }

    /**
     * 根据商品ID查询商品评价（分页）
     *
     * @param itemId
     * @param Level
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponsePagination queryPageComment(String itemId, Integer Level, Integer pageIndex, Integer pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemId", itemId);
        map.put("commentLevel", Level);
        PageHelper.startPage(pageIndex, pageSize);
        List<ItemCommentVO> list = itemsCustomMapper.queryItemComments(map);
        for (ItemCommentVO vo : list) {
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }
        return setPage(list, pageIndex);
    }

    public ResponsePagination setPage(List<?> list, Integer pageIndex) {
        PageInfo<?> pageList = new PageInfo<>(list);
        ResponsePagination page = new ResponsePagination();
        page.setPage(pageIndex);
        page.setRows(list);
        page.setTotal(pageList.getPages());
        page.setRecords(pageList.getTotal());
        return page;
    }

    /**
     * 搜索商品列表
     *
     * @param itemName
     * @param sort
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponsePagination searchItems(String itemName, String sort, Integer pageIndex, Integer pageSize) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("itemName", itemName);
        map.put("sort", sort);
        PageHelper.startPage(pageIndex, pageSize);
        List<SearchItemsVO> list = itemsCustomMapper.searchItems(map);
        return setPage(list, pageIndex);
    }

    /**
     * 根据分类ID搜索商品列表
     *
     * @param catId
     * @param sort
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ResponsePagination searchItemsByThirdCat(Integer catId, String sort, Integer pageIndex, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);
        PageHelper.startPage(pageIndex, pageSize);
        List<SearchItemsVO> list = itemsCustomMapper.searchItemsByThirdCat(map);
        return setPage(list, pageIndex);
    }

    /**
     * 根据规格Ids查询最新的购物车中商品数据（用于刷新渲染购物车里的商品信息）
     *
     * @param specIds
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String ids[] = specIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList, ids);
        return itemsCustomMapper.queryItemsBySpecIds(specIdsList);
    }

    /**
     * 根据规格id查询具体规格信息
     *
     * @param specId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    /**
     * 根据商品Id获取商品主图片
     *
     * @param itemId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNO.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    /**
     * 减少库存
     *
     * @param specId
     * @param buyCount
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, int buyCount) {

        // synchronized 不推荐使用,集群下无用，性能低下
        // 锁数据库：不推荐，导致数据库性能低下
        // 分布式锁 zookeeper redis

        // lockUtil.getLock() -- 加锁

        // 查询库存
//        int stock = 2;
//
//        // 判断库存是否能够减少到0以下
//        if (stock - buyCount < 0){
//            // 提示用户库存不够
//
//        }

        // lockUtil.unLock() -- 解锁

        int result = itemsCustomMapper.decreaseItemSpecStock(specId, buyCount);
        if (result != 1){
            throw new RuntimeException("订单创建失败,原因：库存不足！");
        }
    }

}
