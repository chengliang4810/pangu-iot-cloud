package com.pangu.common.core.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JSON 工具类
 *
 * @author 芋道源码
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static ObjectMapper OBJECT_MAPPER = SpringUtils.getBean(ObjectMapper.class);

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static String toJsonString(Object object) {
        if (ObjectUtil.isNull(object)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public static String toJsonString(Object object, boolean ignoreNull) {
        if (ObjectUtil.isNull(object)) {
            return null;
        }
        try {
            if (ignoreNull){
                OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
            }
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(text, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Dict parseMap(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory().constructType(Dict.class));
        } catch (MismatchedInputException e) {
            // 类型不匹配说明不是json
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Dict> parseArrayMap(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, Dict.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return new ArrayList<>();
        }
        try {
            return OBJECT_MAPPER.readValue(text, OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断Json字符串是否是json数组结构
     *
     * @param json 数据
     * @return boolean
     */
    public static boolean isJsonArray(String json) {
        if (StringUtils.isBlank(json)) {
            return false;
        }
        return JSONUtil.isTypeJSONArray(json);
    }

    /**
     * json字符串字节
     *
     * @param object 对象
     * @return {@link byte[]}
     */
    public static byte[] toJsonStringBytes(Object object) {
        if (ObjectUtil.isNull(object)) {
            return new byte[0];
        }
        String jsonString = JsonUtils.toJsonString(object, true);
        assert jsonString != null;
        return jsonString.getBytes();
    }

    /**
     * 解析ndjson
     * 因jackson无法解析科学计数法，所以使用hutool的json工具
     * @param text  文本
     * @param clazz clazz
     * @return {@link List}<{@link T}>
     */
    @NonNull
    public static <T> List<T> parseNdjson(String text,Class<T> clazz) {
        if (StringUtils.isBlank(text)) {
            return Collections.emptyList();
        }
        List<String> stringList = StrUtil.split(text, StrUtil.C_LF);
        List<T> resultList = new ArrayList<>(stringList.size());
        stringList.forEach(json -> {
            if (StringUtils.isBlank(json)){
                return;
            }
            resultList.add(JsonUtils.parseObject(json, clazz));
        });
        return resultList;
    }
}
