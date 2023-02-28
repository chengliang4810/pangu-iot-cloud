package com.pangu.iot.driver;

/**
 * 数据相关
 *
 * @author pnoker
 */
public interface ValueConstant {
    /**
     * 类型相关
     */
    interface Type {
        String HEX = "hex";
        String BYTE = "byte";
        String SHORT = "short";
        String INT = "int";
        String LONG = "long";
        String FLOAT = "float";
        String DOUBLE = "double";
        String BOOLEAN = "boolean";
        String STRING = "string";
    }
}
