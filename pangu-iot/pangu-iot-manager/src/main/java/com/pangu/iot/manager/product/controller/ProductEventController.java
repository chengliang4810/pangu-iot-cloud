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
import com.pangu.iot.manager.product.domain.bo.ProductEventBO;
import com.pangu.iot.manager.product.domain.bo.ProductEventRuleBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventVO;
import com.pangu.iot.manager.product.service.IProductEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 告警规则控制器
 * 前端访问路由地址为:/manager/product_event
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product_event")
public class ProductEventController extends BaseController {

    private final IProductEventService productEventService;

    /**
     * 新增告警规则触发器
     */
    @SaCheckPermission("manager:product_event:add")
    @Log(title = "告警规则", businessType = BusinessType.INSERT)
    @PostMapping("/trigger")
    public R<Void> addTrigger(@Validated(AddGroup.class) @RequestBody ProductEventRuleBO bo) {
        return toAjax(productEventService.createProductEventRule(bo));
    }

    /**
     * 删除告警规则触发器
     *
     * @param ruleId ruleId
     */
    @SaCheckPermission("manager:product_event:remove")
    @Log(title = "告警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/trigger/{ruleId}")
    public R<Void> remove(@NotNull(message = "ruleId不能为空") @PathVariable Long ruleId) {
        return toAjax(productEventService.deleteProductEventRule(ruleId));
    }


    /**
     * 查询告警规则列表
     */
    @SaCheckPermission("manager:product_event:list")
    @GetMapping("/list")
    public TableDataInfo<ProductEventVO> list(ProductEventBO bo, PageQuery pageQuery) {
        return productEventService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出告警规则列表
     */
    @SaCheckPermission("manager:product_event:export")
    @Log(title = "告警规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductEventBO bo, HttpServletResponse response) {
        List<ProductEventVO> list = productEventService.queryList(bo);
        ExcelUtil.exportExcel(list, "告警规则", ProductEventVO.class, response);
    }

    /**
     * 获取告警规则详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:product_event:query")
    @GetMapping("/{id}")
    public R<ProductEventVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productEventService.queryById(id));
    }

    /**
     * 新增告警规则
     */
    @SaCheckPermission("manager:product_event:add")
    @Log(title = "告警规则", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProductEventBO bo) {
        return toAjax(productEventService.insertByBo(bo));
    }

    /**
     * 修改告警规则
     */
    @SaCheckPermission("manager:product_event:edit")
    @Log(title = "告警规则", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductEventBO bo) {
        return toAjax(productEventService.updateByBo(bo));
    }

    /**
     * 删除告警规则
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:product_event:remove")
    @Log(title = "告警规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(productEventService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

}
