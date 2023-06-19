package org.dromara.manager.driver.domain.vo;

import org.dromara.manager.driver.domain.DriverApplication;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.dromara.common.excel.annotation.ExcelDictFormat;
import org.dromara.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 驱动应用视图对象 iot_driver_application
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DriverApplication.class)
public class DriverApplicationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ExcelProperty(value = "主键ID")
    private Long id;

    /**
     * 驱动ID
     */
    @ExcelProperty(value = "驱动ID")
    private Long driverId;

    /**
     * 应用名称
     */
    @ExcelProperty(value = "应用名称")
    private String applicationName;

    /**
     * 显示名称
     */
    @ExcelProperty(value = "显示名称")
    private String host;

    /**
     * 端口号
     */
    @ExcelProperty(value = "端口号")
    private Long port;


}
