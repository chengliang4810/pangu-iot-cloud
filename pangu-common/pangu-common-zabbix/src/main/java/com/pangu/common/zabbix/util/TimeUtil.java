package com.pangu.common.zabbix.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * 时间工具
 *
 * @author chengliang
 * @date 2023/01/11
 */
public class TimeUtil {

    /**
     * 毫秒
     *
     * @param clock 十位毫秒值（秒）
     * @param ns    ns 纳秒
     * @return long 毫秒
     */
    public static long millisecond(long clock, int ns) {
        return LocalDateTime.ofEpochSecond(clock, ns, ZoneOffset.of("+8")).toEpochSecond(ZoneOffset.of("+8"));
    }

}
