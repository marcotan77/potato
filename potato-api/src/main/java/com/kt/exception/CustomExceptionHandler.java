package com.kt.exception;

import com.kt.reps.ApiResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * 自定义捕获异常类
 * @Author: Tcs
 * @Date: 2020-12-17 11:13
 **/
@RestControllerAdvice
public class CustomExceptionHandler {

    // 上传文件超过500k 捕获异常  MaxUploadSizeExceededException
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ApiResult handlerMaxUploadFile(MaxUploadSizeExceededException exception){
        return ApiResult.errorMsg("文件上传大小不能超过500k，请压缩图片或者降低图片质量再上传");
    }
}
