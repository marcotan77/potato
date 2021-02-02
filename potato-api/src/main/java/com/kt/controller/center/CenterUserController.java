package com.kt.controller.center;

import com.kt.bo.center.CenterUserBO;
import com.kt.controller.BaseController;
import com.kt.pojo.Users;
import com.kt.reps.ApiResult;
import com.kt.resource.FileUpload;
import com.kt.service.center.CenterUserService;
import com.kt.utils.CookieUtils;
import com.kt.utils.DateUtil;
import com.kt.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @program: potato
 * @description
 * @Author: Tcs
 * @Date: 2020-12-16 11:12
 **/
@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("/uploadFace")
    public ApiResult uploadFace(
            @ApiParam(name = "userId", value = "用户Id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        // 定义头像保存的地址
        String fileSpace = fileUpload.getImageUserFaceLocation();
        // 在路径上为每一个用户增加一个userId，用于区分不同用户上传
        String updatePathPrefix = File.separator + userId;
        // 开始文件上传
        if (file != null) {
            FileOutputStream fileOutputStream = null;
            try {
                //获取上传文件的名称
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    String[] fileNameArr = fileName.split("\\.");
                    // 获取文件的后缀
                    String suffix = fileNameArr[fileNameArr.length - 1];

                    if (suffix.equalsIgnoreCase("png") &&
                    suffix.equalsIgnoreCase("jpg")&&
                    suffix.equalsIgnoreCase("jpeg")){
                        return ApiResult.errorMsg("图片格式不正确");
                    }
                    // face-{userId}
                    // 文件名称重组 覆盖式 , 增量式：额外拼接当前时间
                    String newFileName = "face-" + userId + "." + suffix;

                    // 上传的头像最终保存的位置
                    String finalFileName = fileSpace + updatePathPrefix + File.separator + newFileName;

                    // 用于提供给web服务访问的地址
                    updatePathPrefix += ("/"+newFileName);

                    File outFile = new File(finalFileName);

                    if (outFile.getParentFile() != null) {
                        // 创建文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    // 文件输出保存的目录
                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, fileOutputStream);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (fileOutputStream != null){
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        } else {
            return ApiResult.errorMsg("文件不能为空");
        }

        // 获取图片服务地址
        String imageServerUrl = fileUpload.getImageServerUrl();
        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = imageServerUrl+updatePathPrefix+"?t="+ DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        // 更新用户头像到数据库
        Users users  =centerUserService.updateUserFace(userId,finalUserFaceUrl);
        users = setNUllProperty(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);

        // TODO 增加token令牌

        return ApiResult.ok();
    }


    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("/update")
    public ApiResult update(@ApiParam(name = "userId", value = "用户Id", required = true)
                            @RequestParam String userId,
                            @RequestBody @Valid CenterUserBO userBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        Users users = centerUserService.updateUserInfo(userId, userBO);

        if (result.hasErrors()) {
            Map<String, Object> errorMap = getErrors(result);
            return ApiResult.errorMap(errorMap);
        }

        users = setNUllProperty(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), true);

        // TODO 增加token令牌

        return ApiResult.ok(users);
    }

    private Map<String, Object> getErrors(BindingResult result) {
        Map<String, Object> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            // 发生验证错误所对应的某一属性
            String errorField = error.getField();
            // 验证错误的信息
            String message = error.getDefaultMessage();
            map.put(errorField, message);
        }
        return map;
    }

    private Users setNUllProperty(Users usersResult) {
        usersResult.setPassword(null);
        usersResult.setRealname(null);
        usersResult.setBirthday(null);
        usersResult.setCreatedTime(null);
        usersResult.setUpdatedTime(null);
        return usersResult;
    }
}
