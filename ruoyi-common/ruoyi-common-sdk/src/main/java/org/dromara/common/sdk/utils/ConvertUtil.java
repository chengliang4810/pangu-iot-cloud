package org.dromara.common.sdk.utils;

import cn.hutool.core.util.ObjectUtil;
import com.graphbuilder.curve.Point;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;

import java.math.BigDecimal;

/**
 * 类型转换相关工具类集合
 *
 * @author pnoker
 * @since 2022.1.0
 */
@Slf4j
public class ConvertUtil {

    private ConvertUtil() {}

    private static final BigDecimal defaultBase = new BigDecimal(0);
    private static final BigDecimal defaultMultiple = new BigDecimal(1);

    /**
     * 位号数据处理
     * 当出现精度问题，向上调整
     * 例如：byte 类型的数据经过 base 和 multiple 之后超出范围，将其调整为float类型
     *
     * @param point    Point
     * @param rawValue Raw Value
     * @return Value
     */
    public static String convertValue(Point point, String rawValue) {
        if (ObjectUtil.isNull(point)) {
            // throw new EmptyException("Point is empty");
        }

//        PointTypeFlagEnum valueType = Optional.ofNullable(point.getPointTypeFlag()).orElse(PointTypeFlagEnum.STRING);
//        BigDecimal base = Optional.ofNullable(point.getBaseValue()).orElse(defaultBase);
//        BigDecimal multiple = Optional.ofNullable(point.getMultiple()).orElse(defaultMultiple);
//        byte decimal = Optional.ofNullable(point.getValueDecimal()).orElse((byte) 6);
//
//        Object value;
//        switch (valueType) {
//            case BYTE:
//                value = convertByte(rawValue, base, multiple);
//                break;
//            case SHORT:
//                value = convertShort(rawValue, base, multiple);
//                break;
//            case INT:
//                value = convertInteger(rawValue, base, multiple);
//                break;
//            case LONG:
//                value = convertLong(rawValue, base, multiple);
//                break;
//            case FLOAT:
//                value = convertFloat(rawValue, base, multiple, decimal);
//                break;
//            case DOUBLE:
//                value = convertDouble(rawValue, base, multiple, decimal);
//                break;
//            case BOOLEAN:
//                value = convertBoolean(rawValue);
//                break;
//            default:
//                return rawValue;
//        }

//        return String.valueOf(value);
        return "";
    }

    /**
     * 字符串转短字节值
     * -128 ~ 127
     *
     * @param content 字符串
     * @return short
     */
    private static byte convertByte(String content, BigDecimal base, BigDecimal multiple) {
        try {
            BigDecimal multiply = linear(multiple, content, base);
            return multiply.byteValue();
        } catch (Exception e) {
            throw new ServiceException("Out of byte range: {} ~ {}, current: {}", Byte.MIN_VALUE, Byte.MAX_VALUE, content);
        }
    }

    /**
     * 字符串转短整数值
     * -32768 ~ 32767
     *
     * @param content 字符串
     * @return short
     */
    private static short convertShort(String content, BigDecimal base, BigDecimal multiple) {
        try {
            BigDecimal multiply = linear(multiple, content, base);
            return multiply.shortValue();
        } catch (Exception e) {
            throw new ServiceException("Out of short range: {} ~ {}, current: {}", Short.MIN_VALUE, Short.MAX_VALUE, content);
        }
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
     *
     * @param content 字符串
     * @return boolean
     */
    private static boolean convertBoolean(String content) {
        return Boolean.parseBoolean(content);
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
