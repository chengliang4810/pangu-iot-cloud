package com.pangu.common.sdk.tuils;

import cn.hutool.core.util.ReUtil;

/**
 * 验证工具
 *
 * @author chengliang
 * @date 2023/02/28
 */
public class ValidateUtil {

    /**
     * 判断字符串是否为 用户名格式（2-64）
     *
     * @param name String
     * @return boolean
     */
    public static boolean isName(String name) {
        String regex = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_]{1,63}$";
        return ReUtil.isMatch(regex, name);
    }


    /**
     * 判断字符串是否为 Host格式
     *
     * @param host String
     * @return boolean
     */
    public static boolean isHost(String host) {
        String regex = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$";
        return ReUtil.isMatch(regex, host);
    }



}
