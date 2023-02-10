package com.pangu.iot.manager.device.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.pangu.common.core.domain.R;
import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.controller.BaseController;
import com.pangu.common.log.annotation.Log;
import com.pangu.common.log.enums.BusinessType;
import com.pangu.iot.manager.device.domain.bo.DeviceEventRuleBO;
import com.pangu.iot.manager.device.domain.vo.DeviceAlarmRuleVO;
import com.pangu.iot.manager.device.service.IDeviceEventRuleService;
import com.pangu.iot.manager.device.service.IProductAndAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * 设备告警规则控制器
 * 前端访问路由地址为:/manager/device
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/device/alarm/rule")
public class DeviceAlarmRuleController extends BaseController {

    private final IProductAndAttributeService productAndAttributeService;
    private final IDeviceEventRuleService deviceEventRuleService;

    /**
     * 获取设备告警规则
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:device:query")
    @GetMapping("/{id}")
    public R<DeviceAlarmRuleVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deviceEventRuleService.getById(id));
    }

    /**
     * 新增设备告警规则
     */
    @SaCheckPermission("manager:device:add")
    @Log(title = "设备告警规则", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DeviceEventRuleBO bo) {
        return toAjax(deviceEventRuleService.createDeviceEventRule(bo));
    }

    /**
     * 修改设备告警规则
     */
    @SaCheckPermission("manager:device:edit")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DeviceEventRuleBO bo) {
        return toAjax(deviceEventRuleService.updateDeviceEventRuleByBo(bo));
    }

    /**
     * 删除设备
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:device:remove")
    @Log(title = "设备", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(productAndAttributeService.deleteDeviceByIds(Arrays.asList(ids)));
    }

}
