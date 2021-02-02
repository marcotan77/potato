package com.kt.controller;

import com.kt.enums.YesOrNO;
import com.kt.pojo.Carousel;
import com.kt.pojo.Category;
import com.kt.reps.ApiResult;
import com.kt.service.CarouselService;
import com.kt.service.CategoryService;
import com.kt.vo.CategoryVO;
import com.kt.vo.NewItemsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: potato
 * @description 轮播图控制层
 * @Author: Tan.
 * @Date: 2020-12-05 10:29
 **/
@Api(value = "首页", tags = "首页展示的相关接口")
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public ApiResult carousel() {
        List<Carousel> carouselList = carouselService.queryAll(YesOrNO.YES.type);
        return ApiResult.ok(carouselList);
    }

    /**
     * 首页分类展示需求：
     * 1.第一次刷新主页查询打分类，渲染展示到主页
     * 2.如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类(一级商品)", notes = "获取商品分类(一级商品)", httpMethod = "GET")
    @GetMapping("/cats")
    public ApiResult cats() {
        List<Category> list = categoryService.queryRootLevelCat();
        return ApiResult.ok(list);
    }

    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public ApiResult subCat(
            @ApiParam(name = "rootCatId",value = "一级分类Id",required = true)
            @PathVariable Integer rootCatId){
        if (rootCatId == null){
            return ApiResult.errorMsg("分类不存在");
        }
        List<CategoryVO> subCatList = categoryService.getSubCatList(rootCatId);
        return ApiResult.ok(subCatList);
    }

    @ApiOperation(value = "查询每个一级分类下的最新6条商品数据",notes = "查询每个一级分类下的最新6条商品数据",httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public ApiResult sixNewItems(
            @ApiParam(name = "rootCatId",value = "一级分类Id",required = true)
            @PathVariable Integer rootCatId){
        if (rootCatId == null){
            return ApiResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> subCatList = categoryService.getSixNemItemLazy(rootCatId);
        return ApiResult.ok(subCatList);
    }



}
