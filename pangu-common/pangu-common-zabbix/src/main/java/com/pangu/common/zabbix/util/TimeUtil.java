package com.pangu.common.zabbix.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 时间工具
 *
 * @author chengliang
 * @date 2023/01/11
 */
public class TimeUtil {

    /**
     * 转换为DateTime 日期时间
     *
     * @param clock 时钟
     * @return {@link LocalDateTime}
     */
    public static LocalDateTime toLocalDateTime(int clock) {
        return LocalDateTime.ofEpochSecond(clock, 0, ZoneOffset.of("+8"));
    }

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

    /**
     * 到时间戳
     *
     * @param clock 时钟
     * @param ns    ns
     * @return {@link Timestamp}
     */
    public static Timestamp toTimestamp(long clock, Integer ns) {
        return Timestamp.valueOf(LocalDateTime.ofEpochSecond(clock, ns, ZoneOffset.of("+8")));
    }


    public static String formatTime(Date createTime) {

        return null;
    }
}
