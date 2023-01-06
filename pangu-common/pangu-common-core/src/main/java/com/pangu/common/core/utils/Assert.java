package com.pangu.common.core.utils;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.pangu.common.core.exception.ServiceException;
import org.springframework.lang.Nullable;

/**
 * 断言工具类
 *
 * @author chengliang
 * @date 2022/11/11
 */
public class Assert {


    /**
     * 非空检查
     *
     * @param object  对象
     * @param message 消息模板
     * @param args    消息参数
     */
    public static void notNull(@Nullable Object object, String message, Object... args){
        if (ObjectUtil.isNull(object)){
            throw new ServiceException(CharSequenceUtil.format(message, args));
        }
    }

    /**
     * 非空检查
     *
     * @param object  对象
     */
    public static void notNull(@Nullable Object object){
        notNull(object, "[Assertion failed] - this expression must be not null");
    }

    /**
     * 字符串非空检查
     *
     * @param string  字符串
     * @param message 消息模板
     * @param args    消息参数
     */
    public static void notBlank(@Nullable String string, String message, Object... args){
        if (CharSequenceUtil.isEmpty(string)){
            throw new ServiceException(CharSequenceUtil.format(message, args));
        }
    }

    /**
     * 字符串非空检查
     *
     * @param object  对象
     */
    public static void notBlank(@Nullable String object){
        notBlank(object, "[Assertion failed] - this expression must be not blank");
    }


    /**
     * 是TRUE
     *
     * @param expression 表达式
     * @param message    消息
     * @param args       消息参数
     */
    public static void isTrue(Boolean expression, String message, Object... args){
        if (BooleanUtil.isFalse(expression)){
            throw new ServiceException(CharSequenceUtil.format(message, args));
        }
    }

    /**
     * 是TRUE
     *
     * @param expression 表达式
     */
    public static void isTrue(@Nullable Boolean expression){
        isTrue(expression, "[Assertion failed] - this expression must be True");
    }

    /**
     * 是False
     *
     * @param expression 表达式
     * @param message    消息
     * @param args       消息参数
     */
    public static void isFalse(Boolean expression, String message, Object... args){
        if (BooleanUtil.isTrue(expression)){
            throw new ServiceException(CharSequenceUtil.format(message, args));
        }
    }

}
