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
import com.pangu.iot.manager.driver.domain.bo.DriverServiceBO;
import com.pangu.iot.manager.driver.domain.vo.DriverServiceVO;
import com.pangu.iot.manager.driver.service.IDriverServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 驱动服务控制器
 * 前端访问路由地址为:/manager/driver/service
 *
 * @author chengliang4810
 * @date 2023-03-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driver/service")
public class DriverServiceController extends BaseController {

    private final IDriverServiceService driverServiceService;

    /**
     * 查询驱动服务列表
     */
    @SaCheckPermission("manager:driver/service:list")
    @GetMapping("/list")
    public TableDataInfo<DriverServiceVO> list(DriverServiceBO bo, PageQuery pageQuery) {
        return driverServiceService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出驱动服务列表
     */
    @SaCheckPermission("manager:driver/service:export")
    @Log(title = "驱动服务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DriverServiceBO bo, HttpServletResponse response) {
        List<DriverServiceVO> list = driverServiceService.queryList(bo);
        ExcelUtil.exportExcel(list, "驱动服务", DriverServiceVO.class, response);
    }

    /**
     * 获取驱动服务详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driver/service:query")
    @GetMapping("/{id}")
    public R<DriverServiceVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(driverServiceService.queryById(id));
    }

    /**
     * 新增驱动服务
     */
    @SaCheckPermission("manager:driver/service:add")
    @Log(title = "驱动服务", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DriverServiceBO bo) {
        return toAjax(driverServiceService.insertByBo(bo));
    }

    /**
     * 修改驱动服务
     */
    @SaCheckPermission("manager:driver/service:edit")
    @Log(title = "驱动服务", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DriverServiceBO bo) {
        return toAjax(driverServiceService.updateByBo(bo));
    }

    /**
     * 删除驱动服务
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:driver/service:remove")
    @Log(title = "驱动服务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(driverServiceService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
