package com.pangu.iot.manager.device.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.pangu.common.core.domain.R;
import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.controller.BaseController;
import com.pangu.common.excel.utils.ExcelUtil;
import com.pangu.common.log.annotation.Log;
import com.pangu.common.log.enums.BusinessType;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusFunctionBO;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusJudgeRuleBO;
import com.pangu.iot.manager.device.domain.vo.DeviceStatusFunctionVO;
import com.pangu.iot.manager.device.service.IDeviceStatusFunctionService;
import com.pangu.iot.manager.device.service.IDeviceTriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 设备上下线规则控制器
 * 前端访问路由地址为:/manager/device_status_function
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/device_status_function")
public class DeviceStatusFunctionController extends BaseController {

    private final IDeviceStatusFunctionService deviceStatusFunctionService;
    private final IDeviceTriggerService deviceTriggerService;

    /**
     * 新增设备上下线规则触发器
     */
    @SaCheckPermission("manager:device_status_function:add")
    @Log(title = "设备上下线规则触发器", businessType = BusinessType.INSERT)
    @PostMapping("/trigger")
    public R<Void> createTrigger(@Validated(AddGroup.class) @RequestBody DeviceStatusJudgeRuleBO bo) {
        return toAjax(deviceTriggerService.createDeviceStatusJudgeTrigger(bo));
    }

    /**
     * 获取设备上下线规则详细信息
     *
     * @param id 关系Id
     */
    @SaCheckPermission("manager:device_status_function:query")
    @GetMapping("/relationId/{id}")
    public R<DeviceStatusFunctionVO> getInfoByRelationId(@NotNull(message = "关系ID不能为空") @PathVariable Long id) {
        return R.ok(deviceStatusFunctionService.queryRelationId(id));
    }

    /**
     * 查询设备上下线规则列表
     */
    @SaCheckPermission("manager:device_status_function:list")
    @GetMapping("/list")
    public TableDataInfo<DeviceStatusFunctionVO> list(DeviceStatusFunctionBO bo, PageQuery pageQuery) {
        return deviceStatusFunctionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备上下线规则列表
     */
    @SaCheckPermission("manager:device_status_function:export")
    @Log(title = "设备上下线规则", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DeviceStatusFunctionBO bo, HttpServletResponse response) {
        List<DeviceStatusFunctionVO> list = deviceStatusFunctionService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备上下线规则", DeviceStatusFunctionVO.class, response);
    }

    /**
     * 获取设备上下线规则详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:device_status_function:query")
    @GetMapping("/{id}")
    public R<DeviceStatusFunctionVO> relationIdgetInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deviceStatusFunctionService.queryById(id));
    }

    /**
     * 新增设备上下线规则
     */
    @SaCheckPermission("manager:device_status_function:add")
    @Log(title = "设备上下线规则", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DeviceStatusFunctionBO bo) {
        return toAjax(deviceStatusFunctionService.insertByBo(bo));
    }

    /**
     * 修改设备上下线规则
     */
    @SaCheckPermission("manager:device_status_function:edit")
    @Log(title = "设备上下线规则", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DeviceStatusFunctionBO bo) {
        return toAjax(deviceStatusFunctionService.updateByBo(bo));
    }

    /**
     * 删除设备上下线规则
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:device_status_function:remove")
    @Log(title = "设备上下线规则", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(deviceStatusFunctionService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
