package org.dromara.manager.driver.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.manager.driver.domain.Driver;

import java.io.Serial;
import java.io.Serializable;


/**
 * 驱动视图对象 iot_driver
 *
 * @author chengliang4810
 * @date 2023-06-16
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = Driver.class)
public class DriverVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 租户编号
     */
    private String tenantId;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 协议唯一性标识
     */
    @ExcelProperty(value = "协议唯一性标识")
    private String code;

    /**
     * 显示名称
     */
    @ExcelProperty(value = "显示名称")
    private String displayName;

    /**
     * 在线应用数量
     */
    @ExcelProperty(value = "在线应用数量")
    private Long serverNumber = 0L;

    /**
     * 启用|禁用
     */
    @ExcelProperty(value = "启用|禁用")
    private Long enable;

    /**
     * 描述
     */
    @ExcelProperty(value = "描述")
    private String remark;


}
