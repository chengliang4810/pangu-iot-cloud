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
import com.pangu.iot.manager.driver.domain.vo.DriverInfoVO;
import com.pangu.iot.manager.driver.domain.bo.DriverInfoBO;
import com.pangu.iot.manager.driver.service.IDriverInfoService;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.List;
import java.util.Arrays;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.servlet.http.HttpServletResponse;

/**
 * 驱动属性配置信息控制器
 * 前端访问路由地址为:/manager/driver/info
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driver/info")
public class DriverInfoController extends BaseController {

    private final IDriverInfoService driverInfoService;

    /**
     * 查询驱动属性配置信息列表
     */
    @SaCheckPermission("manager:driver/info:list")
    @GetMapping("/list")
    public TableDataInfo<DriverInfoVO> list(DriverInfoBO bo, PageQuery pageQuery) {
        return driverInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出驱动属性配置信息列表
     */
    @SaCheckPermission("manager:driver/info:export")
    @Log(title = "驱动属性配置信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DriverInfoBO bo, HttpServletResponse response) {
        List<DriverInfoVO> list = driverInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "驱动属性配置信息", DriverInfoVO.class, response);
    }

    /**
     * 获取驱动属性配置信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driver/info:query")
    @GetMapping("/{id}")
    public R<DriverInfoVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(driverInfoService.queryById(id));
    }

    /**
     * 新增驱动属性配置信息
     */
    @SaCheckPermission("manager:driver/info:add")
    @Log(title = "驱动属性配置信息", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DriverInfoBO bo) {
        return toAjax(driverInfoService.insertByBo(bo));
    }

    /**
     * 修改驱动属性配置信息
     */
    @SaCheckPermission("manager:driver/info:edit")
    @Log(title = "驱动属性配置信息", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DriverInfoBO bo) {
        return toAjax(driverInfoService.updateByBo(bo));
    }

    /**
     * 删除驱动属性配置信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:driver/info:remove")
    @Log(title = "驱动属性配置信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(driverInfoService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
