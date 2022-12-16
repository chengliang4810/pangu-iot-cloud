package com.pangu.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据权限组
 *
 * @author chengliang4810
 * @version 3.5.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

    DataColumn[] value();

}
