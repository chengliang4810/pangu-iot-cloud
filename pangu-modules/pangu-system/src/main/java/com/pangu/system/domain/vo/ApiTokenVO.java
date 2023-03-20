package com.pangu.system.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pangu.common.excel.annotation.ExcelDictFormat;
import com.pangu.common.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 三方授权视图对象
 *
 * @author chengliang4810
 * @date 2023-03-14
 */
@Data
@ExcelIgnoreUnannotated
public class ApiTokenVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    private Long id;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 过期时间
     */
    @ExcelProperty(value = "过期时间")
    private Date expirationTime;

    /**
     * 状态（0正常 1停用）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private Boolean status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}
