package org.dromara.manager.driver.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.driver.domain.bo.DriverBo;
import org.dromara.manager.driver.domain.vo.DriverVo;
import org.dromara.manager.driver.service.BatchService;
import org.dromara.manager.driver.service.IDriverService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 驱动
 *
 * @author chengliang4810
 * @date 2023-06-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driver")
public class DriverController extends BaseController {

    private final IDriverService driverService;
    private final BatchService batchService;

    /**
     * 查询父级设备（多个）对应的驱动信息列表
     */
    @SaCheckPermission("manager:driver:list")
    @GetMapping("/parentDevice/{deviceId}")
    public R<List<DriverVo>> parentDevice(@NotNull(message = "设备ID不能为空") @PathVariable Long deviceId) {
        return R.ok(batchService.queryParentDeviceDriver(deviceId));
    }

    /**
     * 查询驱动列表
     */
    @SaCheckPermission("manager:driver:list")
    @GetMapping("/list")
    public TableDataInfo<DriverVo> list(DriverBo bo, PageQuery pageQuery) {
        return driverService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询驱动列表
     */
    @SaCheckPermission("manager:driver:list")
    @GetMapping("/tree")
    public R<List<DriverVo>> tree(DriverBo bo) {
        return R.ok(driverService.queryList(bo));
    }

    /**
     * 导出驱动列表
     */
    @SaCheckPermission("manager:driver:export")
    @Log(title = "驱动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DriverBo bo, HttpServletResponse response) {
        List<DriverVo> list = driverService.queryList(bo);
        ExcelUtil.exportExcel(list, "驱动", DriverVo.class, response);
    }

    /**
     * 获取驱动详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driver:query")
    @GetMapping("/{id}")
    public R<DriverVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(driverService.queryById(id));
    }

//    /**
//     * 新增驱动
//     */
//    @SaCheckPermission("manager:driver:add")
//    @Log(title = "驱动", businessType = BusinessType.INSERT)
//    @RepeatSubmit()
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody DriverBo bo) {
//        return toAjax(driverService.insertByBo(bo));
//    }
//
//    /**
//     * 修改驱动
//     */
//    @SaCheckPermission("manager:driver:edit")
//    @Log(title = "驱动", businessType = BusinessType.UPDATE)
//    @RepeatSubmit()
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DriverBo bo) {
//        return toAjax(driverService.updateByBo(bo));
//    }
//
//    /**
//     * 删除驱动
//     *
//     * @param ids 主键串
//     */
//    @SaCheckPermission("manager:driver:remove")
//    @Log(title = "驱动", businessType = BusinessType.DELETE)
//    @DeleteMapping("/{ids}")
//    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
//        return toAjax(driverService.deleteWithValidByIds(List.of(ids), true));
//    }

}
