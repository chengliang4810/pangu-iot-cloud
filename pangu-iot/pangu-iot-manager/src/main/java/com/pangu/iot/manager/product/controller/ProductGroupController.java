package com.pangu.iot.manager.product.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.pangu.common.core.domain.R;
import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.validate.QueryGroup;
import com.pangu.common.core.web.controller.BaseController;
import com.pangu.common.excel.utils.ExcelUtil;
import com.pangu.common.log.annotation.Log;
import com.pangu.common.log.enums.BusinessType;
import com.pangu.common.mybatis.core.page.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.pangu.iot.manager.product.domain.vo.ProductGroupVO;
import com.pangu.iot.manager.product.domain.bo.ProductGroupBO;
import com.pangu.iot.manager.product.service.IProductGroupService;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 产品分组控制器
 * 前端访问路由地址为:/manager/product_group
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product_group")
public class ProductGroupController extends BaseController {

    private final IProductGroupService productGroupService;

    /**
     * 查询产品分组列表
     */
    @SaCheckPermission("manager:product_group:list")
    @GetMapping("/list")
    public R<List<ProductGroupVO>> list(ProductGroupBO bo) {
        List<ProductGroupVO> list = productGroupService.queryList(bo);
        return R.ok(list);
    }

    /**
     * 导出产品分组列表
     */
    @SaCheckPermission("manager:product_group:export")
    @Log(title = "产品分组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductGroupBO bo, HttpServletResponse response) {
        List<ProductGroupVO> list = productGroupService.queryList(bo);
        ExcelUtil.exportExcel(list, "产品分组", ProductGroupVO.class, response);
    }

    /**
     * 获取产品分组详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:product_group:query")
    @GetMapping("/{id}")
    public R<ProductGroupVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productGroupService.queryById(id));
    }

    /**
     * 新增产品分组
     */
    @SaCheckPermission("manager:product_group:add")
    @Log(title = "产品分组", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProductGroupBO bo) {
        return toAjax(productGroupService.insertByBo(bo));
    }

    /**
     * 修改产品分组
     */
    @SaCheckPermission("manager:product_group:edit")
    @Log(title = "产品分组", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductGroupBO bo) {
        return toAjax(productGroupService.updateByBo(bo));
    }

    /**
     * 删除产品分组
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:product_group:remove")
    @Log(title = "产品分组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(productGroupService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
