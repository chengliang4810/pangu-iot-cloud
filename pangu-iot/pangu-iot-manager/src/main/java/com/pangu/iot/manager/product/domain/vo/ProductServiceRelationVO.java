package com.pangu.iot.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;



/**
 * 产品功能关联关系视图对象
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Data
@ExcelIgnoreUnannotated
public class ProductServiceRelationVO {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 服务ID
     */
    @ExcelProperty(value = "服务ID")
    private Long serviceId;

    /**
     * 关联ID
     */
    @ExcelProperty(value = "关联ID")
    private Long relationId;

    /**
     * 是否继承
     */
    @ExcelProperty(value = "是否继承")
    private Boolean inherit;


}
