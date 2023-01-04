package com.pangu.iot.manager.controller;

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
import com.pangu.iot.manager.domain.bo.ProductBO;
import com.pangu.iot.manager.domain.vo.ProductVO;
import com.pangu.iot.manager.service.IProductService;
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
 * 前端访问路由地址为:/iot/product
 *
 * @author chengliang4810
 * @date 2022-12-30
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController extends BaseController {

    private final IProductService productService;

    /**
     * 查询产品列表
     */
    @GetMapping("/list")
    @SaCheckPermission("iot:product:list")
    public TableDataInfo<ProductVO> list(ProductBO bo, PageQuery pageQuery) {
        return productService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出产品列表
     */
    @PostMapping("/export")
    @SaCheckPermission("iot:product:export")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    public void export(ProductBO bo, HttpServletResponse response) {
        List<ProductVO> list = productService.queryList(bo);
        ExcelUtil.exportExcel(list, "产品", ProductVO.class, response);
    }

    /**
     * 获取产品详细信息
     *
     * @param productId 主键
     */
    @GetMapping("/{productId}")
    @SaCheckPermission("iot:product:query")
    public R<ProductVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable String productId) {
        return R.ok(productService.queryById(productId));
    }

    /**
     * 新增产品
     */
    @PostMapping()
    @SaCheckPermission("iot:product:add")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProductBO bo) {
        return toAjax(productService.insertByBo(bo));
    }

    /**
     * 修改产品
     */
    @PutMapping()
    @SaCheckPermission("iot:product:edit")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductBO bo) {
        return toAjax(productService.updateByBo(bo));
    }

    /**
     * 删除产品
     *
     * @param productIds 主键串
     */
    @DeleteMapping("/{productIds}")
    @SaCheckPermission("iot:product:remove")
    @Log(title = "产品", businessType = BusinessType.DELETE)
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable String[] productIds) {
        return toAjax(productService.deleteWithValidByIds(Arrays.asList(productIds), true));
    }

}
