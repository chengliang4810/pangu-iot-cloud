package org.dromara.manager.device.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import org.dromara.common.translation.annotation.Translation;
import org.dromara.manager.device.domain.Device;
import org.dromara.manager.device.translation.DeviceTransConstant;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


/**
 * 设备视图对象 iot_device
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Device.class)
public class DeviceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设备主键
     */
    @ExcelProperty(value = "设备主键")
    private Long id;

    /**
     * 设备分组ID
     */
    @ExcelProperty(value = "设备分组ID")
    private Long groupId;

    /**
     * 产品ID
     */
    @ExcelProperty(value = "产品ID")
    private Long productId;

    /**
     * 设备编号
     */
    @ExcelProperty(value = "设备编号")
    private String code;

    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称")
    private String name;

    /**
     * 设备类型
     */
    private Integer deviceType;

    /**
     * 设备地址
     */
    @ExcelProperty(value = "设备地址")
    private String address;

    /**
     * 地址坐标
     */
    @ExcelProperty(value = "地址坐标")
    private String position;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 启用状态
     */
    @ExcelProperty(value = "启用状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "iot_enable_status")
    private Integer status;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String remark;


    /**
     * 子设备数量
     */
    @ExcelProperty(value = "子设备数量")
    @Translation(type = DeviceTransConstant.GATEWAY_ID_TO_CHILD_DEVICE_NUMBER, mapper = "id")
    private Long childDeviceNumber;

}
