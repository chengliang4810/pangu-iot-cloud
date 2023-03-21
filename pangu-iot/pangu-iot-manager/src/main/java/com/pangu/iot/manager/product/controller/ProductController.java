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
import com.pangu.iot.manager.device.service.IProductAndAttributeService;
import com.pangu.iot.manager.product.domain.bo.ProductBO;
import com.pangu.iot.manager.product.domain.vo.ProductVO;
import com.pangu.iot.manager.product.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 产品控制器
 * 前端访问路由地址为:/manager/product
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    private final IProductService productService;

    private final IProductAndAttributeService productAndAttributeService;

    /**
     * 分页查询产品列表
     */
    @SaCheckPermission("manager:product:list")
    @GetMapping("/list")
    public TableDataInfo<ProductVO> list(ProductBO bo, PageQuery pageQuery) {
        return productService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询产品列表
     */
    @SaCheckPermission("manager:product:list")
    @GetMapping("/tree")
    public R<List<ProductVO>> tree(ProductBO bo) {
        return R.ok(productService.queryList(bo));
    }

    /**
     * 导出产品列表
     */
    @SaCheckPermission("manager:product:export")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductBO bo, HttpServletResponse response) {
        List<ProductVO> list = productService.queryList(bo);
        ExcelUtil.exportExcel(list, "产品", ProductVO.class, response);
    }

    /**
     * 获取产品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:product:query")
    @GetMapping("/{id}")
    public R<ProductVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productService.queryById(id));
    }

    /**
     * 新增产品
     */
    @SaCheckPermission("manager:product:add")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProductBO bo) {
        return toAjax(productService.insertByBo(bo));
    }

    /**
     * 修改产品
     */
    @SaCheckPermission("manager:product:edit")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductBO bo) {
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
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(productAndAttributeService.deleteProductByIds(Arrays.asList(ids)));
    }

}
