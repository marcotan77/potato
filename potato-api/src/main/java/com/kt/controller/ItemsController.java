package com.kt.controller;

import com.kt.pojo.Items;
import com.kt.pojo.ItemsImg;
import com.kt.pojo.ItemsParam;
import com.kt.pojo.ItemsSpec;
import com.kt.reps.ApiResult;
import com.kt.service.ItemService;
import com.kt.utils.ResponsePagination;
import com.kt.vo.CommentLevelCountVO;
import com.kt.vo.ItemInfoVO;
import com.kt.vo.ShopcartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: potato
 * @description
 * @Author: Tan.
 * @Date: 2020-12-05 16:01
 **/
@Api(value = "商品接口", tags = {"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {

    @Autowired
    private ItemService itemService;


    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public ApiResult info(
            @ApiParam(name = "itemId", value = "商品Id", required = true)
            @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return ApiResult.errorMsg(null);
        }
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        itemInfoVO.setItemParams(itemsParam);
        return ApiResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public ApiResult commentLevel(
            @ApiParam(name = "itemId", value = "商品Id", required = true)
            @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return ApiResult.errorMsg(null);
        }
        CommentLevelCountVO countVO = itemService.queryCommentCounts(itemId);
        return ApiResult.ok(countVO);
    }

    @ApiOperation(value = "查询商品评价", notes = "查询商品评价", httpMethod = "GET")
    @GetMapping("/comments")
    public ApiResult comments(
            @ApiParam(name = "itemId", value = "商品Id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评级等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize
    ) {
        if (StringUtils.isBlank(itemId)) {
            return ApiResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        ResponsePagination responsePagination = itemService.queryPageComment(itemId, level, page, pageSize);
        return ApiResult.ok(responsePagination);
    }

    @ApiOperation(value = "搜商品列表", notes = "搜商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public ApiResult search(
            @ApiParam(name = "keywords", value = "关键字", required = true, example = "")
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序", required = false, example = "c")
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false, example = "1")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false, example = "20")
            @RequestParam Integer pageSize
    ) {
        if (StringUtils.isBlank(keywords)) {
            return ApiResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        ResponsePagination responsePagination = itemService.searchItems(keywords, sort, page, pageSize);
        return ApiResult.ok(responsePagination);
    }

    @ApiOperation(value = "通过分类ID搜商品列表", notes = "通过分类ID搜商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public ApiResult catItems(
            @ApiParam(name = "catId", value = "三级分类Id", required = true, example = "")
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序", required = false, example = "c")
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false, example = "1")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false, example = "20")
            @RequestParam Integer pageSize
    ) {
        if (null == catId) {
            return ApiResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        ResponsePagination responsePagination = itemService.searchItemsByThirdCat(catId, sort, page, pageSize);
        return ApiResult.ok(responsePagination);
    }

    // 用于用户长时间未登录网点，刷新购物车中的数据（主要是商品价格），类似京东淘宝
    @ApiOperation(value = "根据商品规格Ids查找最新的商品数据",notes = "根据商品规格Ids查找最新的商品数据",httpMethod = "GET")
    @GetMapping("/refresh")
    public ApiResult refresh(
            @ApiParam(name = "itemSpecIds",value = "拼接的规格Ids",required = true,example = "1001,1002")
            @RequestParam String itemSpecIds
    ){
        if (StringUtils.isBlank(itemSpecIds)){
            return ApiResult.ok();
        }
        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);
        return ApiResult.ok(list);
    }

}
