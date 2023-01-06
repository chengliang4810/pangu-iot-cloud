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
import com.pangu.iot.manager.device.domain.bo.DeviceAttributeBO;
import com.pangu.iot.manager.device.domain.vo.DeviceAttributeVO;
import com.pangu.iot.manager.device.service.IDeviceAttributeService;
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
 * 设备属性控制器
 * 前端访问路由地址为:/manager/attribute
 *
 * @author chengliang4810
 * @date 2023-01-05
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/device_attribute")
public class DeviceAttributeController extends BaseController {

    private final IDeviceAttributeService deviceAttributeService;

    private final IProductAndAttributeService productAndAttributeService;

    /**
     * 查询设备属性列表
     */
    @SaCheckPermission("manager:attribute:list")
    @GetMapping("/list")
    public TableDataInfo<DeviceAttributeVO> list(DeviceAttributeBO bo, PageQuery pageQuery) {
        return deviceAttributeService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询设备属性列表
     */
    @SaCheckPermission("manager:attribute:list")
    @GetMapping("/tree")
    public R<List<DeviceAttributeVO>> list(DeviceAttributeBO bo) {
        return R.ok(deviceAttributeService.queryList(bo));
    }

    /**
     * 导出设备属性列表
     */
    @SaCheckPermission("manager:attribute:export")
    @Log(title = "设备属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DeviceAttributeBO bo, HttpServletResponse response) {
        List<DeviceAttributeVO> list = deviceAttributeService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备属性", DeviceAttributeVO.class, response);
    }

    /**
     * 获取设备属性详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:attribute:query")
    @GetMapping("/{id}")
    public R<DeviceAttributeVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deviceAttributeService.queryById(id));
    }

    /**
     * 新增设备属性
     */
    @SaCheckPermission("manager:attribute:add")
    @Log(title = "设备属性", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DeviceAttributeBO bo) {
        return toAjax(productAndAttributeService.insertAttribute(bo));
    }

    /**
     * 修改设备属性
     */
    @SaCheckPermission("manager:attribute:edit")
    @Log(title = "设备属性", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DeviceAttributeBO bo) {
        return toAjax(productAndAttributeService.updateAttribute(bo));
    }

    /**
     * 删除设备属性
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:attribute:remove")
    @Log(title = "设备属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(deviceAttributeService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
