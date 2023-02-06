package com.pangu.iot.manager.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.pangu.iot.manager.product.domain.ProductServiceParam;
import lombok.Data;

import java.util.List;


/**
 * 产品功能视图对象
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Data
@ExcelIgnoreUnannotated
public class ProductServiceVO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 功能名称
     */
    @ExcelProperty(value = "功能名称")
    private String name;

    /**
     * 功能标识
     */
    @ExcelProperty(value = "功能标识")
    private String mark;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;

    /**
     * 执行方式 0-同步 1-异步
     */
    @ExcelProperty(value = "执行方式 0-同步 1-异步")
    private Long async;

    /**
     * 产品服务参数列表
     */
    private List<ProductServiceParam> productServiceParamList;


}