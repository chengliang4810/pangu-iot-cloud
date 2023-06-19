package org.dromara.manager.driver.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.manager.driver.domain.vo.PointAttributeVo;
import org.dromara.manager.driver.domain.bo.PointAttributeBo;
import org.dromara.manager.driver.service.IPointAttributeService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 驱动属性
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/pointAttribute")
public class PointAttributeController extends BaseController {

    private final IPointAttributeService pointAttributeService;

    /**
     * 查询驱动属性列表
     */
    @SaCheckPermission("manager:pointAttribute:list")
    @GetMapping("/list")
    public TableDataInfo<PointAttributeVo> list(PointAttributeBo bo, PageQuery pageQuery) {
        return pointAttributeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出驱动属性列表
     */
    @SaCheckPermission("manager:pointAttribute:export")
    @Log(title = "驱动属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PointAttributeBo bo, HttpServletResponse response) {
        List<PointAttributeVo> list = pointAttributeService.queryList(bo);
        ExcelUtil.exportExcel(list, "驱动属性", PointAttributeVo.class, response);
    }

    /**
     * 获取驱动属性详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:pointAttribute:query")
    @GetMapping("/{id}")
    public R<PointAttributeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(pointAttributeService.queryById(id));
    }

    /**
     * 新增驱动属性
     */
    @SaCheckPermission("manager:pointAttribute:add")
    @Log(title = "驱动属性", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PointAttributeBo bo) {
        return toAjax(pointAttributeService.insertByBo(bo));
    }

    /**
     * 修改驱动属性
     */
    @SaCheckPermission("manager:pointAttribute:edit")
    @Log(title = "驱动属性", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PointAttributeBo bo) {
        return toAjax(pointAttributeService.updateByBo(bo));
    }

    /**
     * 删除驱动属性
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:pointAttribute:remove")
    @Log(title = "驱动属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(pointAttributeService.deleteWithValidByIds(List.of(ids), true));
    }
}
