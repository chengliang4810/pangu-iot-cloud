package com.pangu.common.zabbix.annotation;


/**
 * @author nantian
 * <p>
 * 标记单个值 name
 */

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ParamName {

    String value() default "";
}
