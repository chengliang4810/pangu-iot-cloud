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
import com.pangu.iot.manager.product.domain.bo.ProductEventExpressionBO;
import com.pangu.iot.manager.product.domain.vo.ProductEventExpressionVO;
import com.pangu.iot.manager.product.service.IProductEventExpressionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 告警规则达式控制器
 * 前端访问路由地址为:/manager/product_event_expression
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product_event_expression")
public class ProductEventExpressionController extends BaseController {

    private final IProductEventExpressionService productEventExpressionService;

    /**
     * 查询告警规则达式列表
     */
    @SaCheckPermission("manager:product_event_expression:list")
    @GetMapping("/list")
    public TableDataInfo<ProductEventExpressionVO> list(ProductEventExpressionBO bo, PageQuery pageQuery) {
        return productEventExpressionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出告警规则达式列表
     */
    @SaCheckPermission("manager:product_event_expression:export")
    @Log(title = "告警规则达式", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductEventExpressionBO bo, HttpServletResponse response) {
        List<ProductEventExpressionVO> list = productEventExpressionService.queryList(bo);
        ExcelUtil.exportExcel(list, "告警规则达式", ProductEventExpressionVO.class, response);
    }

    /**
     * 获取告警规则达式详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:product_event_expression:query")
    @GetMapping("/{id}")
    public R<ProductEventExpressionVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productEventExpressionService.queryById(id));
    }

    /**
     * 新增告警规则达式
     */
    @SaCheckPermission("manager:product_event_expression:add")
    @Log(title = "告警规则达式", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProductEventExpressionBO bo) {
        return toAjax(productEventExpressionService.insertByBo(bo));
    }

    /**
     * 修改告警规则达式
     */
    @SaCheckPermission("manager:product_event_expression:edit")
    @Log(title = "告警规则达式", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductEventExpressionBO bo) {
        return toAjax(productEventExpressionService.updateByBo(bo));
    }

    /**
     * 删除告警规则达式
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:product_event_expression:remove")
    @Log(title = "告警规则达式", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(productEventExpressionService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
