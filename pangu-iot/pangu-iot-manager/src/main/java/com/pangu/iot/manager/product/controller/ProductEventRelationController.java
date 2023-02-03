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
import com.pangu.iot.manager.product.domain.vo.ProductEventRelationVO;
import com.pangu.iot.manager.product.domain.bo.ProductEventRelationBO;
import com.pangu.iot.manager.product.service.IProductEventRelationService;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 告警规则关系控制器
 * 前端访问路由地址为:/manager/product_event_relation
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product_event_relation")
public class ProductEventRelationController extends BaseController {

    private final IProductEventRelationService productEventRelationService;

    /**
     * 查询告警规则关系列表
     */
    @SaCheckPermission("manager:product_event_relation:list")
    @GetMapping("/list")
    public TableDataInfo<ProductEventRelationVO> list(ProductEventRelationBO bo, PageQuery pageQuery) {
        return productEventRelationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出告警规则关系列表
     */
    @SaCheckPermission("manager:product_event_relation:export")
    @Log(title = "告警规则关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProductEventRelationBO bo, HttpServletResponse response) {
        List<ProductEventRelationVO> list = productEventRelationService.queryList(bo);
        ExcelUtil.exportExcel(list, "告警规则关系", ProductEventRelationVO.class, response);
    }

    /**
     * 获取告警规则关系详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:product_event_relation:query")
    @GetMapping("/{id}")
    public R<ProductEventRelationVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(productEventRelationService.queryById(id));
    }

    /**
     * 新增告警规则关系
     */
    @SaCheckPermission("manager:product_event_relation:add")
    @Log(title = "告警规则关系", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProductEventRelationBO bo) {
        return toAjax(productEventRelationService.insertByBo(bo));
    }

    /**
     * 修改告警规则关系
     */
    @SaCheckPermission("manager:product_event_relation:edit")
    @Log(title = "告警规则关系", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProductEventRelationBO bo) {
        return toAjax(productEventRelationService.updateByBo(bo));
    }

    /**
     * 删除告警规则关系
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:product_event_relation:remove")
    @Log(title = "告警规则关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(productEventRelationService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
