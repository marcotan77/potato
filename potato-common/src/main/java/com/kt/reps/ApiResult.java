package com.kt.reps;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @program: potato
 * @description 自定义相应数据结构
 * @Author: Tan.
 * @Date: 2020-12-03 15:50
 **/
public class ApiResult {
    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;

    @JsonIgnore
    private String ok;	// 不使用
    public static ApiResult build(Integer status, String msg, Object data) {
        return new ApiResult(status, msg, data);
    }

    public static ApiResult build(Integer status, String msg, Object data, String ok) {
        return new ApiResult(status, msg, data, ok);
    }

    public static ApiResult ok(Object data) {
        return new ApiResult(data);
    }

    public static ApiResult ok() {
        return new ApiResult(null);
    }

    public static ApiResult errorMsg(String msg) {
        return new ApiResult(500, msg, null);
    }

    public static ApiResult errorMap(Object data) {
        return new ApiResult(501, "error", data);
    }

    public static ApiResult errorTokenMsg(String msg) {
        return new ApiResult(502, msg, null);
    }

    public static ApiResult errorException(String msg) {
        return new ApiResult(555, msg, null);
    }

    public static ApiResult errorUserQQ(String msg) {
        return new ApiResult(556, msg, null);
    }

    public ApiResult() {

    }

    public ApiResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public ApiResult(Integer status, String msg, Object data, String ok) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.ok = ok;
    }

    public ApiResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Boolean isOK() {
        return this.status == 200;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }
}
