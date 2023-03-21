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
import com.pangu.iot.manager.device.domain.bo.DeviceStatusFunctionRelationBO;
import com.pangu.iot.manager.device.domain.vo.DeviceStatusFunctionRelationVO;
import com.pangu.iot.manager.device.service.IDeviceStatusFunctionRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 设备上下线规则与设备关系控制器
 * 前端访问路由地址为:/manager/relation
 *
 * @author chengliang4810
 * @date 2023-02-02
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/relation")
public class DeviceStatusFunctionRelationController extends BaseController {

    private final IDeviceStatusFunctionRelationService deviceStatusFunctionRelationService;

    /**
     * 查询设备上下线规则与设备关系列表
     */
    @SaCheckPermission("manager:relation:list")
    @GetMapping("/list")
    public TableDataInfo<DeviceStatusFunctionRelationVO> list(DeviceStatusFunctionRelationBO bo, PageQuery pageQuery) {
        return deviceStatusFunctionRelationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设备上下线规则与设备关系列表
     */
    @SaCheckPermission("manager:relation:export")
    @Log(title = "设备上下线规则与设备关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DeviceStatusFunctionRelationBO bo, HttpServletResponse response) {
        List<DeviceStatusFunctionRelationVO> list = deviceStatusFunctionRelationService.queryList(bo);
        ExcelUtil.exportExcel(list, "设备上下线规则与设备关系", DeviceStatusFunctionRelationVO.class, response);
    }

    /**
     * 获取设备上下线规则与设备关系详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:relation:query")
    @GetMapping("/{id}")
    public R<DeviceStatusFunctionRelationVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(deviceStatusFunctionRelationService.queryById(id));
    }

    /**
     * 新增设备上下线规则与设备关系
     */
    @SaCheckPermission("manager:relation:add")
    @Log(title = "设备上下线规则与设备关系", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DeviceStatusFunctionRelationBO bo) {
        return toAjax(deviceStatusFunctionRelationService.insertByBo(bo));
    }

    /**
     * 修改设备上下线规则与设备关系
     */
    @SaCheckPermission("manager:relation:edit")
    @Log(title = "设备上下线规则与设备关系", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DeviceStatusFunctionRelationBO bo) {
        return toAjax(deviceStatusFunctionRelationService.updateByBo(bo));
    }

    /**
     * 删除设备上下线规则与设备关系
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:relation:remove")
    @Log(title = "设备上下线规则与设备关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(deviceStatusFunctionRelationService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
