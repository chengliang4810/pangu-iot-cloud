package org.dromara.manager.product.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.product.domain.bo.ProductBo;
import org.dromara.manager.product.domain.vo.ProductVo;
import org.dromara.manager.product.service.IProductService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    private final IProductService productService;

    /**
     * 查询产品列表
     */
    @SaCheckPermission("manager:product:list")
    @GetMapping("/list")
    public TableDataInfo<ProductVo> list(ProductBo bo, PageQuery pageQuery) {
        return productService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询产品列表
     */
    @SaCheckPermission("manager:product:list")
    @GetMapping("/tree")
    public R<List<ProductVo>> tree(ProductBo bo) {
        return R.ok(productService.queryList(bo));
    }

    /**
     * 导出产品列表
     */
    @SaCheckPermission("manager:product:export")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductBo bo, HttpServletResponse response) {
        List<ProductVo> list = productService.queryList(bo);
        ExcelUtil.exportExcel(list, "产品", ProductVo.class, response);
    }

    /**
     * 获取产品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:product:query")
    @GetMapping("/{id}")
    public R<ProductVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(productService.queryById(id));
    }

    /**
     * 新增产品
     */
    @SaCheckPermission("manager:product:add")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProductBo bo) {
        return toAjax(productService.insertByBo(bo));
    }

    /**
     * 修改产品
     */
    @SaCheckPermission("manager:product:edit")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductBo bo) {
        return toAjax(productService.updateByBo(bo));
    }

    /**
     * 删除产品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:product:remove")
    @Log(title = "产品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(productService.deleteWithValidByIds(List.of(ids), true));
    }
}
