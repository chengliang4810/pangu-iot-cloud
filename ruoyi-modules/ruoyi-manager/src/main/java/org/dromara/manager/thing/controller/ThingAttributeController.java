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
import org.dromara.manager.thing.domain.bo.ThingAttributeBo;
import org.dromara.manager.thing.domain.vo.ThingAttributeVo;
import org.dromara.manager.thing.service.IThingAttributeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物模型属性
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/thing/attribute")
public class ThingAttributeController extends BaseController {

    private final IThingAttributeService deviceAttributeService;

    /**
     * 查询某个物模型属性实时数据
     */
    @SaCheckPermission("manager:deviceAttribute:list")
    @GetMapping("/device/{deviceId}")
    public R<List<ThingAttributeVo>> realTimeData(@NotNull(message = "设备编号不能为空") @PathVariable Long deviceId, @RequestParam(defaultValue = "false") Boolean realTime) {
        return R.ok(deviceAttributeService.queryListByDeviceId(deviceId, realTime));
    }

    /**
     * 查询物模型属性列表
     */
    @SaCheckPermission("manager:deviceAttribute:list")
    @GetMapping("/list")
    public TableDataInfo<ThingAttributeVo> list(ThingAttributeBo bo, PageQuery pageQuery) {
        return deviceAttributeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出物模型属性列表
     */
    @SaCheckPermission("manager:deviceAttribute:export")
    @Log(title = "物模型属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ThingAttributeBo bo, HttpServletResponse response) {
        List<ThingAttributeVo> list = deviceAttributeService.queryList(bo);
        ExcelUtil.exportExcel(list, "物模型属性", ThingAttributeVo.class, response);
    }

    /**
     * 获取物模型属性详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:deviceAttribute:query")
    @GetMapping("/{id}")
    public R<ThingAttributeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(deviceAttributeService.queryById(id));
    }

    /**
     * 新增物模型属性
     */
    @SaCheckPermission("manager:deviceAttribute:add")
    @Log(title = "物模型属性", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ThingAttributeBo bo) {
        return toAjax(deviceAttributeService.insertByBo(bo));
    }

    /**
     * 修改物模型属性
     */
    @SaCheckPermission("manager:deviceAttribute:edit")
    @Log(title = "物模型属性", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ThingAttributeBo bo) {
        return toAjax(deviceAttributeService.updateByBo(bo));
    }

    /**
     * 删除物模型属性
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:deviceAttribute:remove")
    @Log(title = "物模型属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(deviceAttributeService.deleteWithValidByIds(List.of(ids), true));
    }
}
