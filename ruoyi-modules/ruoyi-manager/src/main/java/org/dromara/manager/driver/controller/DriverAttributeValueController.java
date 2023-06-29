package org.dromara.manager.driver.controller;

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
import org.dromara.manager.driver.domain.bo.DriverAttributeValueBo;
import org.dromara.manager.driver.domain.vo.DriverAttributeValueVo;
import org.dromara.manager.driver.service.IDriverAttributeValueService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 驱动属性值
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driverAttributeValue")
public class DriverAttributeValueController extends BaseController {

    private final IDriverAttributeValueService driverAttributeValueService;

    /**
     * 查询驱动属性值列表
     */
    @SaCheckPermission("manager:driverAttributeValue:list")
    @GetMapping("/list")
    public TableDataInfo<DriverAttributeValueVo> list(DriverAttributeValueBo bo, PageQuery pageQuery) {
        return driverAttributeValueService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询驱动属性值列表
     */
    @SaCheckPermission("manager:driverAttributeValue:list")
    @GetMapping("/tree")
    public R<List<DriverAttributeValueVo>> tree(DriverAttributeValueBo bo) {
        return R.ok(driverAttributeValueService.queryList(bo));
    }


    /**
     * 导出驱动属性值列表
     */
    @SaCheckPermission("manager:driverAttributeValue:export")
    @Log(title = "驱动属性值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DriverAttributeValueBo bo, HttpServletResponse response) {
        List<DriverAttributeValueVo> list = driverAttributeValueService.queryList(bo);
        ExcelUtil.exportExcel(list, "驱动属性值", DriverAttributeValueVo.class, response);
    }

    /**
     * 获取驱动属性值详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driverAttributeValue:query")
    @GetMapping("/{id}")
    public R<DriverAttributeValueVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(driverAttributeValueService.queryById(id));
    }

    /**
     * 新增驱动属性值
     */
    @SaCheckPermission("manager:driverAttributeValue:add")
    @Log(title = "驱动属性值", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DriverAttributeValueBo bo) {
        return toAjax(driverAttributeValueService.insertByBo(bo));
    }

    /**
     * 新增驱动属性值
     */
    @SaCheckPermission("manager:driverAttributeValue:add")
    @Log(title = "驱动属性值", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/batch")
    public R<Void> addBatch(@NotEmpty(message = "驱动属性不能为空") @RequestBody List<DriverAttributeValueBo> bos) {
        return toAjax(driverAttributeValueService.batchInsertByBo(bos));
    }

    /**
     * 修改驱动属性值
     */
    @SaCheckPermission("manager:driverAttributeValue:edit")
    @Log(title = "驱动属性值", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DriverAttributeValueBo bo) {
        return toAjax(driverAttributeValueService.updateByBo(bo));
    }

    /**
     * 删除驱动属性值
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:driverAttributeValue:remove")
    @Log(title = "驱动属性值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(driverAttributeValueService.deleteWithValidByIds(List.of(ids), true));
    }
}
