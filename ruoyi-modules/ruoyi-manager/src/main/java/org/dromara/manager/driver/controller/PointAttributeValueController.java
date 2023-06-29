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
import org.dromara.manager.driver.domain.bo.PointAttributeValueBo;
import org.dromara.manager.driver.domain.vo.PointAttributeValueVo;
import org.dromara.manager.driver.service.IPointAttributeValueService;
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
@RequestMapping("/pointAttributeValue")
public class PointAttributeValueController extends BaseController {

    private final IPointAttributeValueService pointAttributeValueService;

    /**
     * 查询驱动属性值列表
     */
    @SaCheckPermission("manager:pointAttributeValue:list")
    @GetMapping("/list")
    public TableDataInfo<PointAttributeValueVo> list(PointAttributeValueBo bo, PageQuery pageQuery) {
        return pointAttributeValueService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询驱动属性值列表
     */
    @SaCheckPermission("manager:pointAttributeValue:list")
    @GetMapping("/tree")
    public R<List<PointAttributeValueVo>> tree(PointAttributeValueBo bo) {
        return R.ok(pointAttributeValueService.queryList(bo));
    }

    /**
     * 导出驱动属性值列表
     */
    @SaCheckPermission("manager:pointAttributeValue:export")
    @Log(title = "驱动属性值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PointAttributeValueBo bo, HttpServletResponse response) {
        List<PointAttributeValueVo> list = pointAttributeValueService.queryList(bo);
        ExcelUtil.exportExcel(list, "驱动属性值", PointAttributeValueVo.class, response);
    }

    /**
     * 获取驱动属性值详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:pointAttributeValue:query")
    @GetMapping("/{id}")
    public R<PointAttributeValueVo> getInfo(@NotNull(message = "主键不能为空")
                                            @PathVariable Long id) {
        return R.ok(pointAttributeValueService.queryById(id));
    }

    /**
     * 新增驱动属性值
     */
    @SaCheckPermission("manager:pointAttributeValue:add")
    @Log(title = "驱动属性值", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PointAttributeValueBo bo) {
        return toAjax(pointAttributeValueService.insertByBo(bo));
    }

    /**
     * 修改驱动属性值
     */
    @SaCheckPermission("manager:pointAttributeValue:edit")
    @Log(title = "驱动属性值", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PointAttributeValueBo bo) {
        return toAjax(pointAttributeValueService.updateByBo(bo));
    }

    /**
     * 删除驱动属性值
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:pointAttributeValue:remove")
    @Log(title = "驱动属性值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(pointAttributeValueService.deleteWithValidByIds(List.of(ids), true));
    }
}
