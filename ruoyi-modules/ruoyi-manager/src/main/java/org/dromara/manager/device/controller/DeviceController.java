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
import org.dromara.manager.device.domain.bo.DeviceBo;
import org.dromara.manager.device.domain.vo.DeviceVo;
import org.dromara.manager.device.service.IDeviceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController {

    private final IDeviceService deviceService;

    /**
     * 查询设备列表
     */
    @SaCheckPermission("manager:device:list")
    @GetMapping("/list")
    public TableDataInfo<DeviceVo> list(DeviceBo bo, PageQuery pageQuery) {
        return deviceService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备列表
     */
    @SaCheckPermission("manager:device:export")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DeviceBo bo, HttpServletResponse response) {
        List<DeviceVo> list = deviceService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备", DeviceVo.class, response);
    }

    /**
     * 获取设备详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:device:query")
    @GetMapping("/{id}")
    public R<DeviceVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(deviceService.queryById(id));
    }

    /**
     * 新增设备
     */
    @SaCheckPermission("manager:device:add")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DeviceBo bo) {
        return toAjax(deviceService.insertByBo(bo));
    }

    /**
     * 修改设备
     */
    @SaCheckPermission("manager:device:edit")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DeviceBo bo) {
        return toAjax(deviceService.updateByBo(bo));
    }

    /**
     * 删除设备
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:device:remove")
    @Log(title = "设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(deviceService.deleteWithValidByIds(List.of(ids), true));
    }
}
