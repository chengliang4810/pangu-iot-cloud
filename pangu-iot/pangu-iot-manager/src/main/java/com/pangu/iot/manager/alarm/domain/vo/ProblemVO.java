package com.pangu.iot.manager.alarm.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;



/**
 * 告警记录视图对象
 *
 * @author chengliang4810
 * @date 2023-02-13
 */
@Data
@ExcelIgnoreUnannotated
public class ProblemVO {

    private static final long serialVersionUID = 1L;

    /**
     * event_id
     */
    @ExcelProperty(value = "event_id")
    private Long eventId;

    /**
     * 对象ID
     */
    @ExcelProperty(value = "对象ID")
    private Long objectId;

    @ExcelProperty(value = "告警级别")
    private Integer severity;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 待确认状态
     */
    @ExcelProperty(value = "待确认状态")
    private Long acknowledged;

    /**
     * 承认
     */
    private String acknowledgedStr;

    /**
     * 时间
     */
    @ExcelProperty(value = "时间")
    private LocalDateTime clock;

    /**
     * 解决时间
     */
    @ExcelProperty(value = "解决时间")
    private LocalDateTime rClock;

    /**
     * 设备ID
     */
    @ExcelProperty(value = "设备ID")
    private Long deviceId;

    /**
     * 状态
     */
    private String statusStr;

    public String getAcknowledgedStr() {
        return acknowledged == 0 ? "未确认" : "已确认";
    }

    public String getStatusStr() {
       return this.getRClock() == null ? "未解决" : "已解决";
    }

}
