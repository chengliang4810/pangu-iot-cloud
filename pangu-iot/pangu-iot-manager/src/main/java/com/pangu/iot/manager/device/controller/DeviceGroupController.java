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
import com.pangu.iot.manager.device.domain.bo.DeviceGroupBO;
import com.pangu.iot.manager.device.domain.vo.DeviceGroupVO;
import com.pangu.iot.manager.device.service.IDeviceGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 设备分组控制器
 * 前端访问路由地址为:/manager/device_group
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/device_group")
public class DeviceGroupController extends BaseController {

    private final IDeviceGroupService deviceGroupService;

    /**
     * 分页查询设备分组列表
     */
    @SaCheckPermission("manager:device_group:list")
    @GetMapping("/list")
    public TableDataInfo<DeviceGroupVO> list(DeviceGroupBO bo, PageQuery pageQuery) {
        return deviceGroupService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询设备分组列表
     */
    @SaCheckPermission("manager:device_group:list")
    @GetMapping("/tree")
    public R<List<DeviceGroupVO>> tree(DeviceGroupBO bo) {
        return R.ok(deviceGroupService.queryList(bo));
    }

    /**
     * 导出设备分组列表
     */
    @SaCheckPermission("manager:device_group:export")
    @Log(title = "设备分组", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DeviceGroupBO bo, HttpServletResponse response) {
        List<DeviceGroupVO> list = deviceGroupService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备分组", DeviceGroupVO.class, response);
    }

    /**
     * 获取设备分组详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:device_group:query")
    @GetMapping("/{id}")
    public R<DeviceGroupVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deviceGroupService.queryById(id));
    }

    /**
     * 新增设备分组
     */
    @SaCheckPermission("manager:device_group:add")
    @Log(title = "设备分组", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DeviceGroupBO bo) {
        return toAjax(deviceGroupService.insertByBo(bo));
    }

    /**
     * 修改设备分组
     */
    @SaCheckPermission("manager:device_group:edit")
    @Log(title = "设备分组", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DeviceGroupBO bo) {
        return toAjax(deviceGroupService.updateByBo(bo));
    }

    /**
     * 删除设备分组
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:device_group:remove")
    @Log(title = "设备分组", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(deviceGroupService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
