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
import com.pangu.iot.manager.device.domain.bo.ServiceExecuteRecordBO;
import com.pangu.iot.manager.device.domain.vo.ServiceExecuteRecordVO;
import com.pangu.iot.manager.device.service.IServiceExecuteRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 功能执行记录控制器
 * 前端访问路由地址为:/manager/service_execute_record
 *
 * @author chengliang4810
 * @date 2023-02-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/service_execute_record")
public class ServiceExecuteRecordController extends BaseController {

    private final IServiceExecuteRecordService serviceExecuteRecordService;

    /**
     * 查询功能执行记录列表
     */
    @SaCheckPermission("manager:service_execute_record:list")
    @GetMapping("/list")
    public TableDataInfo<ServiceExecuteRecordVO> list(ServiceExecuteRecordBO bo, PageQuery pageQuery) {
        return serviceExecuteRecordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出功能执行记录列表
     */
    @SaCheckPermission("manager:service_execute_record:export")
    @Log(title = "功能执行记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ServiceExecuteRecordBO bo, HttpServletResponse response) {
        List<ServiceExecuteRecordVO> list = serviceExecuteRecordService.queryList(bo);
        ExcelUtil.exportExcel(list, "功能执行记录", ServiceExecuteRecordVO.class, response);
    }

    /**
     * 获取功能执行记录详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:service_execute_record:query")
    @GetMapping("/{id}")
    public R<ServiceExecuteRecordVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(serviceExecuteRecordService.queryById(id));
    }

    /**
     * 新增功能执行记录
     */
    @SaCheckPermission("manager:service_execute_record:add")
    @Log(title = "功能执行记录", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ServiceExecuteRecordBO bo) {
        return toAjax(serviceExecuteRecordService.insertByBo(bo));
    }

    /**
     * 修改功能执行记录
     */
    @SaCheckPermission("manager:service_execute_record:edit")
    @Log(title = "功能执行记录", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ServiceExecuteRecordBO bo) {
        return toAjax(serviceExecuteRecordService.updateByBo(bo));
    }

    /**
     * 删除功能执行记录
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:service_execute_record:remove")
    @Log(title = "功能执行记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(serviceExecuteRecordService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
