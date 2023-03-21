package com.pangu.iot.manager.product.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.pangu.common.core.domain.R;
import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.controller.BaseController;
import com.pangu.common.excel.utils.ExcelUtil;
import com.pangu.common.log.annotation.Log;
import com.pangu.common.log.enums.BusinessType;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.product.domain.bo.ProductServiceBO;
import com.pangu.iot.manager.product.domain.vo.ProductServiceVO;
import com.pangu.iot.manager.product.service.IProductServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 产品功能控制器
 * 前端访问路由地址为:/manager/product_service
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product_service")
public class ProductServiceController extends BaseController {

    private final IProductServiceService productServiceService;

    /**
     * 查询产品功能列表
     */
    @SaCheckPermission("manager:product_service:list")
    @GetMapping("/list")
    public TableDataInfo<ProductServiceVO> list(ProductServiceBO bo, PageQuery pageQuery) {
        return productServiceService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询产品功能树表
     */
    @SaCheckPermission("manager:product_service:list")
    @GetMapping("/tree")
    public R<List<ProductServiceVO>> tree(ProductServiceBO bo) {
        return R.ok(productServiceService.queryList(bo));
    }

    /**
     * 导出产品功能列表
     */
    @SaCheckPermission("manager:product_service:export")
    @Log(title = "产品功能", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductServiceBO bo, HttpServletResponse response) {
        List<ProductServiceVO> list = productServiceService.queryList(bo);
        ExcelUtil.exportExcel(list, "产品功能", ProductServiceVO.class, response);
    }

    /**
     * 获取产品功能详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:product_service:query")
    @GetMapping("/{id}")
    public R<ProductServiceVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productServiceService.queryById(id));
    }

    /**
     * 新增产品功能
     */
    @SaCheckPermission("manager:product_service:add")
    @Log(title = "产品功能", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProductServiceBO bo) {
        return toAjax(productServiceService.insertByBo(bo));
    }

    /**
     * 修改产品功能
     */
    @SaCheckPermission("manager:product_service:edit")
    @Log(title = "产品功能", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductServiceBO bo) {
        return toAjax(productServiceService.updateByBo(bo));
    }

    /**
     * 删除产品功能
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:product_service:remove")
    @Log(title = "产品功能", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(productServiceService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
