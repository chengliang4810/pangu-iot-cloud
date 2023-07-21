package org.dromara.manager.thing.controller;

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
import org.dromara.manager.thing.domain.bo.ThingFunctionBo;
import org.dromara.manager.thing.domain.vo.ThingFunctionVo;
import org.dromara.manager.thing.service.IThingFunctionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物模型功能
 *
 * @author chengliang4810
 * @date 2023-07-20
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/thing/function")
public class ThingFunctionController extends BaseController {

    private final IThingFunctionService deviceFunctionService;

    /**
     * 查询物模型功能列表
     */
    @SaCheckPermission("manager:deviceFunction:list")
    @GetMapping("/list")
    public TableDataInfo<ThingFunctionVo> list(ThingFunctionBo bo, PageQuery pageQuery) {
        return deviceFunctionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出物模型功能列表
     */
    @SaCheckPermission("manager:deviceFunction:export")
    @Log(title = "物模型功能", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ThingFunctionBo bo, HttpServletResponse response) {
        List<ThingFunctionVo> list = deviceFunctionService.queryList(bo);
        ExcelUtil.exportExcel(list, "物模型功能", ThingFunctionVo.class, response);
    }

    /**
     * 获取物模型功能详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:deviceFunction:query")
    @GetMapping("/{id}")
    public R<ThingFunctionVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(deviceFunctionService.queryById(id));
    }

    /**
     * 新增物模型功能
     */
    @SaCheckPermission("manager:deviceFunction:add")
    @Log(title = "物模型功能", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ThingFunctionBo bo) {
        return toAjax(deviceFunctionService.insertByBo(bo));
    }

    /**
     * 修改物模型功能
     */
    @SaCheckPermission("manager:deviceFunction:edit")
    @Log(title = "物模型功能", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ThingFunctionBo bo) {
        return toAjax(deviceFunctionService.updateByBo(bo));
    }

    /**
     * 删除物模型功能
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:deviceFunction:remove")
    @Log(title = "物模型功能", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(deviceFunctionService.deleteWithValidByIds(List.of(ids), true));
    }
}
