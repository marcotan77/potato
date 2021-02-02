package com.kt.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;

/**
 * @program: potato
 * @description MD5工具类
 * @Author: Tan.
 * @Date: 2020-12-03 16:26
 **/
public class MD5Utils {

    public static String getMD5Str(String strValue) throws Exception{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String newStr = Base64.encodeBase64String(md5.digest(strValue.getBytes()));
        return newStr;
    }

}
