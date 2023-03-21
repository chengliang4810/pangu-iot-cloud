package com.pangu.common.core.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.pangu.common.core.utils.JsonUtils;
import org.mapstruct.Named;

import java.util.List;

/**
 * 常见转换方法
 *
 * @author chengliang4810
 * @date 2022/12/13 13:02
 */
public interface CommonConvert {

    /**
     * 列表转 ，字符串
     *
     * @param list 列表
     * @return {@link String}
     */
    @Named("listToString")
    default String listToString(List<String> list) {
        if (CollUtil.isEmpty(list)) {
            return "";
        }
        return String.join("," , list);
    }

    /**
     * 字符串转换为List
     *
     * @param str str 字符串
     * @return {@link List}<{@link String}>
     */
    @Named("stringToList")
    default List<String> stringToList(String str) {
        if (str == null) {
            return ListUtil.empty();
        }
        // 分隔字符串
        return ListUtil.of(str.split(","));
    }

    @Named("jsonToList")
    default <T> List<T> jsonToList(String json, Class<T> tClass) {
        if (StrUtil.isBlank(json)) {
            return ListUtil.empty();
        }
        // 分隔字符串
        return JsonUtils.parseArray(json, tClass);
    }

    @Named("jsonToObject")
    default <T> T jsonToObject(String json, Class<T> tClass) {
        if (StrUtil.isBlank(json)) {
            return null;
        }
        // 分隔字符串
        return JsonUtils.parseObject(json, tClass);
    }


    @Named("intToBool")
    default boolean intToBoolean(Integer value) {
        if (value == null) {
            return false;
        }
        return value == 1;
    }


    @Named("longToBool")
    default boolean longToBool(Long value) {
        if (value == null) {
            return false;
        }
        return value == 1;
    }

    @Named("boolToLong")
    default Long boolToLong(Boolean value) {
        if (value == null) {
            return 0L;
        }
        return Boolean.TRUE.equals(value) ? 1L : 0L;
    }


}
