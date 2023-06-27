package org.dromara.manager.device.controller;

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
import org.dromara.manager.device.domain.bo.DeviceAttributeBo;
import org.dromara.manager.device.domain.vo.DeviceAttributeVo;
import org.dromara.manager.device.service.IDeviceAttributeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备属性
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/deviceAttribute")
public class DeviceAttributeController extends BaseController {

    private final IDeviceAttributeService deviceAttributeService;

    /**
     * 查询设备属性列表
     */
    @SaCheckPermission("manager:deviceAttribute:list")
    @GetMapping("/list")
    public TableDataInfo<DeviceAttributeVo> list(DeviceAttributeBo bo, PageQuery pageQuery) {
        return deviceAttributeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备属性列表
     */
    @SaCheckPermission("manager:deviceAttribute:export")
    @Log(title = "设备属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DeviceAttributeBo bo, HttpServletResponse response) {
        List<DeviceAttributeVo> list = deviceAttributeService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备属性", DeviceAttributeVo.class, response);
    }

    /**
     * 获取设备属性详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:deviceAttribute:query")
    @GetMapping("/{id}")
    public R<DeviceAttributeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(deviceAttributeService.queryById(id));
    }

    /**
     * 新增设备属性
     */
    @SaCheckPermission("manager:deviceAttribute:add")
    @Log(title = "设备属性", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DeviceAttributeBo bo) {
        return toAjax(deviceAttributeService.insertByBo(bo));
    }

    /**
     * 修改设备属性
     */
    @SaCheckPermission("manager:deviceAttribute:edit")
    @Log(title = "设备属性", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DeviceAttributeBo bo) {
        return toAjax(deviceAttributeService.updateByBo(bo));
    }

    /**
     * 删除设备属性
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:deviceAttribute:remove")
    @Log(title = "设备属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(deviceAttributeService.deleteWithValidByIds(List.of(ids), true));
    }
}
