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
import com.pangu.iot.manager.driver.domain.bo.PointAttributeBO;
import com.pangu.iot.manager.driver.domain.vo.DriverPointConfigVO;
import com.pangu.iot.manager.driver.domain.vo.PointAttributeVO;
import com.pangu.iot.manager.driver.service.IPointAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 点位属性控制器
 * 前端访问路由地址为:/manager/driver/point/attribute
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driver/point/attribute")
public class PointAttributeController extends BaseController {

    private final IPointAttributeService pointAttributeService;

    /**
     * 通过设备ID查询驱动点位信息以及对应的点位属性
     *
     * @param deviceId 设备ID
     * @return {@link R}<{@link List}<{@link DriverPointConfigVO}>>
     */
    @GetMapping("/device/{deviceId}/device_attribute/{attributeId}")
    public R<List<DriverPointConfigVO>> getDriverPointConfigByDeviceId(@PathVariable Long deviceId, @PathVariable Long attributeId) {
        return R.ok(pointAttributeService.getDriverPointConfigByDeviceId(deviceId, attributeId));
    }

    /**
     * 查询点位属性列表
     */
    @SaCheckPermission("manager:driver/point/attribute:list")
    @GetMapping("/list")
    public TableDataInfo<PointAttributeVO> list(PointAttributeBO bo, PageQuery pageQuery) {
        return pointAttributeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出点位属性列表
     */
    @SaCheckPermission("manager:driver/point/attribute:export")
    @Log(title = "点位属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PointAttributeBO bo, HttpServletResponse response) {
        List<PointAttributeVO> list = pointAttributeService.queryList(bo);
        ExcelUtil.exportExcel(list, "点位属性", PointAttributeVO.class, response);
    }

    /**
     * 获取点位属性详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driver/point/attribute:query")
    @GetMapping("/{id}")
    public R<PointAttributeVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(pointAttributeService.queryById(id));
    }

    /**
     * 新增点位属性
     */
    @SaCheckPermission("manager:driver/point/attribute:add")
    @Log(title = "点位属性", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PointAttributeBO bo) {
        return toAjax(pointAttributeService.insertByBo(bo));
    }

    /**
     * 修改点位属性
     */
    @SaCheckPermission("manager:driver/point/attribute:edit")
    @Log(title = "点位属性", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PointAttributeBO bo) {
        return toAjax(pointAttributeService.updateByBo(bo));
    }

    /**
     * 删除点位属性
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:driver/point/attribute:remove")
    @Log(title = "点位属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(pointAttributeService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
