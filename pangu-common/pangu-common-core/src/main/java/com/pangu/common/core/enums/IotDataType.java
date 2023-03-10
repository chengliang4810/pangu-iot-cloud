package com.pangu.common.core.enums;

import lombok.Getter;

@Getter
public enum IotDataType {

    /**
     * 整数
     */
    integer("整数") {
        @Override
        public <T> T convert(String jsonString) {
            return null;
        }
    },
    /**
     * 小数
     */
    decimal("小数"){
        @Override
        public <T> T convert(String jsonString) {
            return null;
        }
    },
    /**
     * 数组
     */
    array("数组"){
        @Override
        public <T> T convert(String jsonString) {
            return null;
        }
    },

    bool("布尔"){
        @Override
        public <T> T convert(String jsonString) {
            return null;
        }
    },
    /**
     * 枚举
     */
    enums("枚举"){
        @Override
        public <T> T convert(String jsonString) {
            return null;
        }
    },
    /**
     * 字符串
     */
    string("字符串"){
        @Override
        public <T> T convert(String jsonString) {
            return null;
        }
    };

    /**
     * 描述
     */
    private String desc;


    IotDataType(String desc) {
        this.desc = desc;
    }

    /**
     * 转换实体方法
     *
     * @param jsonString json字符串
     * @return {@link T}
     */
    public abstract <T> T convert(String jsonString);

}
