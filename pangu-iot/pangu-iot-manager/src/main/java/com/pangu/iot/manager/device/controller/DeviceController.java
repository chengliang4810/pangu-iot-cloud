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
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.domain.vo.DeviceListVO;
import com.pangu.iot.manager.device.domain.vo.DeviceVO;
import com.pangu.iot.manager.device.service.IDeviceService;
import com.pangu.iot.manager.device.service.IProductAndAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 设备控制器
 * 前端访问路由地址为:/manager/device
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController {

    private final IDeviceService deviceService;
    private final IProductAndAttributeService productAndAttributeService;


    /**
     * 分页查询设备列表
     */
    @SaCheckPermission("manager:device:list")
    @GetMapping("/list")
    public TableDataInfo<DeviceListVO> list(DeviceBO bo, PageQuery pageQuery) {
        return deviceService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询设备
     */
    @SaCheckPermission("manager:device:list")
    @GetMapping("/tree")
    public R<List<DeviceVO>> tree(DeviceBO bo) {
        return R.ok(deviceService.queryList(bo));
    }

    /**
     * 导出设备列表
     */
    @SaCheckPermission("manager:device:export")
    @Log(title = "设备", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DeviceBO bo, HttpServletResponse response) {
        List<DeviceVO> list = deviceService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备", DeviceVO.class, response);
    }

    /**
     * 获取设备详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:device:query")
    @GetMapping("/{id}")
    public R<DeviceVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deviceService.queryById(id));
    }

    /**
     * 新增设备
     */
    @SaCheckPermission("manager:device:add")
    @Log(title = "设备", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DeviceBO bo) {
        return toAjax(productAndAttributeService.insertDevice(bo));
    }

    /**
     * 修改设备
     */
    @SaCheckPermission("manager:device:edit")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DeviceBO bo) {
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
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(deviceService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
