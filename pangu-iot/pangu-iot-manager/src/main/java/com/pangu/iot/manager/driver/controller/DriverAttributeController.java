package com.pangu.iot.manager.driver.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.pangu.common.core.domain.R;
import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.validate.QueryGroup;
import com.pangu.common.core.web.controller.BaseController;
import com.pangu.common.excel.utils.ExcelUtil;
import com.pangu.common.log.annotation.Log;
import com.pangu.common.log.enums.BusinessType;
import com.pangu.common.mybatis.core.page.PageQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.pangu.iot.manager.driver.domain.vo.DriverAttributeVO;
import com.pangu.iot.manager.driver.domain.bo.DriverAttributeBO;
import com.pangu.iot.manager.driver.service.IDriverAttributeService;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 驱动属性控制器
 * 前端访问路由地址为:/manager/driver/attribute
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driver/attribute")
public class DriverAttributeController extends BaseController {

    private final IDriverAttributeService driverAttributeService;

    /**
     * 查询驱动属性列表
     */
    @SaCheckPermission("manager:driver/attribute:list")
    @GetMapping("/list")
    public TableDataInfo<DriverAttributeVO> list(DriverAttributeBO bo, PageQuery pageQuery) {
        return driverAttributeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出驱动属性列表
     */
    @SaCheckPermission("manager:driver/attribute:export")
    @Log(title = "驱动属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DriverAttributeBO bo, HttpServletResponse response) {
        List<DriverAttributeVO> list = driverAttributeService.queryList(bo);
        ExcelUtil.exportExcel(list, "驱动属性", DriverAttributeVO.class, response);
    }

    /**
     * 获取驱动属性详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driver/attribute:query")
    @GetMapping("/{id}")
    public R<DriverAttributeVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(driverAttributeService.queryById(id));
    }

    /**
     * 新增驱动属性
     */
    @SaCheckPermission("manager:driver/attribute:add")
    @Log(title = "驱动属性", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DriverAttributeBO bo) {
        return toAjax(driverAttributeService.insertByBo(bo));
    }

    /**
     * 修改驱动属性
     */
    @SaCheckPermission("manager:driver/attribute:edit")
    @Log(title = "驱动属性", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DriverAttributeBO bo) {
        return toAjax(driverAttributeService.updateByBo(bo));
    }

    /**
     * 删除驱动属性
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:driver/attribute:remove")
    @Log(title = "驱动属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(driverAttributeService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
