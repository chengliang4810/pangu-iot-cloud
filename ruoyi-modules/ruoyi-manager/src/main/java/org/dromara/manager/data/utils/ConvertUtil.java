package org.dromara.manager.data.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.iot.enums.PointTypeFlagEnum;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * 类型转换相关工具类集合
 *
 * @author pnoker，chengliang4810
 * @since 2022.1.0
 */
@Slf4j
public class ConvertUtil {

    private ConvertUtil() {}

    /**
     * 默认基础
     */
    private static final BigDecimal defaultBase = new BigDecimal(0);
    /**
     * 默认倍数
     */
    private static final BigDecimal defaultMultiple = new BigDecimal(1);
    /**
     * 浮点数小数点精确的位数
     */
    private static final byte decimal = 8;

    /**
     * 位号数据处理
     * 当出现精度问题，向上调整
     * 例如：byte 类型的数据经过 base 和 multiple 之后超出范围，将其调整为float类型
     *
     * @param attributeType    属性类型
     * @param rawValue Raw Value
     * @return Value
     */
    public static String convertValue(String attributeType, String rawValue) {

        PointTypeFlagEnum valueType = Optional.ofNullable(PointTypeFlagEnum.ofCode(attributeType)).orElse(PointTypeFlagEnum.STRING);

        Object value;
        switch (valueType) {
            case INT:
                value = convertInteger(rawValue, defaultBase, defaultMultiple);
                break;
            case LONG:
                value = convertLong(rawValue, defaultBase, defaultMultiple);
                break;
            case FLOAT:
                value = convertFloat(rawValue, defaultBase, defaultMultiple, decimal);
                break;
            case DOUBLE:
                value = convertDouble(rawValue, defaultBase, defaultMultiple, decimal);
                break;
            case BOOLEAN:
                value = convertBoolean(rawValue);
                break;
            case JSON:
                value = convertJson(rawValue);
                break;
            case DATE:
                value = convertDate(rawValue);
                break;
            default:
                return rawValue;
        }

        return String.valueOf(value);
    }

    /**
     * 日期转换
     *
     * @param rawValue 值
     * @return {@link Long}
     */
    private static Long convertDate(String rawValue) {
        // 将字符串转换为时间戳
        return DateUtil.parse(rawValue).getTime();
    }

    /**
     * string转换成json
     *
     * @param rawValue 字符串
     * @return {@link String}
     */
    private static String convertJson(String rawValue) {
        if (JSONUtil.isTypeJSON(rawValue)){
            return rawValue;
        }
        throw new ServiceException("rowValue is not json, rawValue: {}", rawValue);
    }

    /**
     * 字符串转整数值
     * -2147483648 ~ 2147483647
     *
     * @param content 字符串
     * @return int
     */
    private static int convertInteger(String content, BigDecimal base, BigDecimal multiple) {
        try {
            BigDecimal multiply = linear(multiple, content, base);
            return multiply.intValue();
        } catch (Exception e) {
            throw new ServiceException("Out of int range: {} ~ {}, current: {}", Integer.MIN_VALUE, Integer.MAX_VALUE, content);
        }
    }

    /**
     * 字符串转长整数值
     * -9223372036854775808 ~ 9223372036854775807
     *
     * @param content 字符串
     * @return long
     */
    private static long convertLong(String content, BigDecimal base, BigDecimal multiple) {
        try {
            BigDecimal multiply = linear(multiple, content, base);
            return multiply.longValue();
        } catch (Exception e) {
            throw new ServiceException("Out of long range: {} ~ {}, current: {}", Long.MIN_VALUE, Long.MAX_VALUE, content);
        }
    }

    /**
     * 字符串转浮点值
     *
     * @param content 字符串
     * @return float
     */
    private static float convertFloat(String content, BigDecimal base, BigDecimal multiple, byte decimal) {
        try {
            BigDecimal multiply = linear(multiple, content, base);
            if (Float.isInfinite(multiply.floatValue())) {
                throw new ServiceException();
            }
            return ArithmeticUtil.round(multiply.floatValue(), decimal);
        } catch (Exception e) {
            throw new ServiceException("Out of float range: |{} ~ {}|, current: {}", Float.MIN_VALUE, Float.MAX_VALUE, content);
        }
    }

    /**
     * 字符串转双精度浮点值
     *
     * @param content 字符串
     * @return double
     */
    private static double convertDouble(String content, BigDecimal base, BigDecimal multiple, byte decimal) {
        try {
            BigDecimal multiply = linear(multiple, content, base);
            if (Double.isInfinite(multiply.doubleValue())) {
                throw new ServiceException();
            }
            return ArithmeticUtil.round(multiply.doubleValue(), decimal);
        } catch (Exception e) {
            throw new ServiceException("Out of double range: |{} ~ {}|, current: {}", Double.MIN_VALUE, Double.MAX_VALUE, content);
        }
    }

    /**
     * 字符串转布尔值
     * 以下情况为true:  "true", "yes", "y", "t", "ok", "1", "on", "是", "对", "真", "對", "√"
     * 其他为: false
     * @param content 字符串
     * @return boolean
     */
    private static boolean convertBoolean(String content) {
        return BooleanUtil.toBoolean(content);
    }

    /**
     * 线性函数：y = ax + b
     *
     * @param x A
     * @param b B
     * @param a X
     * @return BigDecimal
     */
    private static BigDecimal linear(BigDecimal a, String x, BigDecimal b) {
        BigDecimal bigDecimal = new BigDecimal(x);
        if (defaultMultiple.compareTo(a) == 0 && defaultBase.compareTo(b) == 0) {
            return bigDecimal;
        }
        if (defaultMultiple.compareTo(a) != 0 && defaultBase.compareTo(b) == 0) {
            return bigDecimal.multiply(a);
        }
        if (defaultMultiple.compareTo(a) == 0 && defaultBase.compareTo(b) != 0) {
            return bigDecimal.add(b);
        }
        BigDecimal multiply = a.multiply(bigDecimal);
        return multiply.add(b);
    }
}
