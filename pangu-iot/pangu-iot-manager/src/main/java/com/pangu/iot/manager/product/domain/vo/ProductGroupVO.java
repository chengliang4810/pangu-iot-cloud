package com.pangu.iot.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 产品分组视图对象
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Data
@ExcelIgnoreUnannotated
public class ProductGroupVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 名称
     */
    @ExcelProperty(value = "名称")
    private String name;

    /**
     * 父级ID
     */
    @ExcelProperty(value = "父级ID")
    private Long parentId;

    /**
     * 所有父级ID
     */
    @ExcelProperty(value = "所有父级ID")
    private String pids;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
