package com.pangu.iot.manager.device.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
 * 设备与分组关系视图对象
 *
 * @author chengliang4810
 * @date 2023-01-07
 */
@Data
@ExcelIgnoreUnannotated
public class DeviceGroupRelationVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 设备ID
     */
    @ExcelProperty(value = "设备ID")
    private Long deviceId;

    /**
     * 设备组ID
     */
    @ExcelProperty(value = "设备组ID")
    private Long deviceGroupId;


}
