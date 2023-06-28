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
import org.dromara.manager.driver.domain.bo.DriverAttributeBo;
import org.dromara.manager.driver.domain.vo.DriverAttributeVo;
import org.dromara.manager.driver.service.IDriverAttributeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 驱动属性
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driverAttribute")
public class DriverAttributeController extends BaseController {

    private final IDriverAttributeService driverAttributeService;

    /**
     * 查询驱动属性列表
     */
    @SaCheckPermission("manager:driverAttribute:list")
    @GetMapping("/tree")
    public R<List<DriverAttributeVo>> tree(DriverAttributeBo bo) {
        return R.ok(driverAttributeService.queryList(bo));
    }

    /**
     * 查询驱动属性列表
     */
    @SaCheckPermission("manager:driverAttribute:list")
    @GetMapping("/list")
    public TableDataInfo<DriverAttributeVo> list(DriverAttributeBo bo, PageQuery pageQuery) {
        return driverAttributeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出驱动属性列表
     */
    @SaCheckPermission("manager:driverAttribute:export")
    @Log(title = "驱动属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DriverAttributeBo bo, HttpServletResponse response) {
        List<DriverAttributeVo> list = driverAttributeService.queryList(bo);
        ExcelUtil.exportExcel(list, "驱动属性", DriverAttributeVo.class, response);
    }

    /**
     * 获取驱动属性详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driverAttribute:query")
    @GetMapping("/{id}")
    public R<DriverAttributeVo> getInfo(@NotNull(message = "主键不能为空")
                                        @PathVariable Long id) {
        return R.ok(driverAttributeService.queryById(id));
    }

    /**
     * 新增驱动属性
     */
    @SaCheckPermission("manager:driverAttribute:add")
    @Log(title = "驱动属性", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DriverAttributeBo bo) {
        return toAjax(driverAttributeService.insertByBo(bo));
    }

    /**
     * 修改驱动属性
     */
    @SaCheckPermission("manager:driverAttribute:edit")
    @Log(title = "驱动属性", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DriverAttributeBo bo) {
        return toAjax(driverAttributeService.updateByBo(bo));
    }

    /**
     * 删除驱动属性
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:driverAttribute:remove")
    @Log(title = "驱动属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(driverAttributeService.deleteWithValidByIds(List.of(ids), true));
    }
}
