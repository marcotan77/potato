package com.kt.service.impl;

import com.github.pagehelper.PageInfo;
import com.kt.utils.ResponsePagination;

import java.util.List;

/**
 * @Author: Tcs
 * @Date: 2020-12-18 10:57
 **/
public class BaseService {
    public ResponsePagination setPage(List<?> list, Integer pageIndex) {
        PageInfo<?> pageList = new PageInfo<>(list);
        ResponsePagination page = new ResponsePagination();
        page.setPage(pageIndex);
        page.setRows(list);
        page.setTotal(pageList.getPages());
        page.setRecords(pageList.getTotal());
        return page;
    }
}
