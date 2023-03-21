package com.pangu.iot.manager.driver.controller;

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
import com.pangu.iot.manager.driver.domain.bo.DriverBO;
import com.pangu.iot.manager.driver.domain.vo.DriverConfigVO;
import com.pangu.iot.manager.driver.domain.vo.DriverVO;
import com.pangu.iot.manager.driver.service.IDriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 协议驱动控制器
 * 前端访问路由地址为:/manager/driver
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driver")
public class DriverController extends BaseController {

    private final IDriverService driverService;


    /**
     * 通过设备ID查询驱动信息以及对应的驱动属性
     *
     * @param deviceId 设备ID
     * @return {@link R}<{@link List}<{@link DriverConfigVO}>>
     */
    @GetMapping("/attribute/device/{deviceId}")
    public R<List<DriverConfigVO>> attributeProduct(@PathVariable Long deviceId) {
        return R.ok(driverService.getDriverConfigByDeviceId(deviceId));
    }


    /**
     * 查询协议驱动表所有数据
     */
    @GetMapping("/tree")
    public R<List<DriverVO>> tree(DriverBO bo) {
        return R.ok(driverService.queryList(bo));
    }

    /**
     * 查询协议驱动列表
     */
    @SaCheckPermission("manager:driver:list")
    @GetMapping("/list")
    public TableDataInfo<DriverVO> list(DriverBO bo, PageQuery pageQuery) {
        return driverService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出协议驱动列表
     */
    @SaCheckPermission("manager:driver:export")
    @Log(title = "协议驱动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DriverBO bo, HttpServletResponse response) {
        List<DriverVO> list = driverService.queryList(bo);
        ExcelUtil.exportExcel(list, "协议驱动", DriverVO.class, response);
    }

    /**
     * 获取协议驱动详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driver:query")
    @GetMapping("/{id}")
    public R<DriverVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(driverService.queryById(id));
    }

    /**
     * 新增协议驱动
     */
    @SaCheckPermission("manager:driver:add")
    @Log(title = "协议驱动", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DriverBO bo) {
        return toAjax(driverService.insertByBo(bo));
    }

    /**
     * 修改协议驱动
     */
    @SaCheckPermission("manager:driver:edit")
    @Log(title = "协议驱动", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DriverBO bo) {
        return toAjax(driverService.updateByBo(bo));
    }

    /**
     * 删除协议驱动
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:driver:remove")
    @Log(title = "协议驱动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(driverService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
