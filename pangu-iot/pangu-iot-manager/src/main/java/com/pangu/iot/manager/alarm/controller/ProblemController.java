package com.pangu.iot.manager.alarm.controller;

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
import com.pangu.iot.manager.alarm.domain.bo.ProblemBO;
import com.pangu.iot.manager.alarm.domain.vo.ProblemVO;
import com.pangu.iot.manager.alarm.service.IProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 告警记录控制器
 * 前端访问路由地址为:/manager/problem
 *
 * @author chengliang4810
 * @date 2023-02-13
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/problem")
public class ProblemController extends BaseController {

    private final IProblemService problemService;

    /**
     * 查询告警记录列表
     */
    @SaCheckPermission("manager:problem:list")
    @GetMapping("/list")
    public TableDataInfo<ProblemVO> list(ProblemBO bo, PageQuery pageQuery) {
        return problemService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出告警记录列表
     */
    @SaCheckPermission("manager:problem:export")
    @Log(title = "告警记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ProblemBO bo, HttpServletResponse response) {
        List<ProblemVO> list = problemService.queryList(bo);
        ExcelUtil.exportExcel(list, "告警记录", ProblemVO.class, response);
    }

    /**
     * 获取告警记录详细信息
     *
     * @param eventId 主键
     */
    @SaCheckPermission("manager:problem:query")
    @GetMapping("/{eventId}")
    public R<ProblemVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long eventId) {
        return R.ok(problemService.queryById(eventId));
    }

    /**
     * 新增告警记录
     */
    @SaCheckPermission("manager:problem:add")
    @Log(title = "告警记录", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ProblemBO bo) {
        return toAjax(problemService.insertByBo(bo));
    }

    /**
     * 修改告警记录
     */
    @SaCheckPermission("manager:problem:edit")
    @Log(title = "告警记录", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ProblemBO bo) {
        return toAjax(problemService.updateByBo(bo));
    }

    /**
     * 删除告警记录
     *
     * @param eventIds 主键串
     */
    @SaCheckPermission("manager:problem:remove")
    @Log(title = "告警记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{eventIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] eventIds) {
        return toAjax(problemService.deleteWithValidByIds(Arrays.asList(eventIds), true));
    }
}
