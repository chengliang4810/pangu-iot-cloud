package org.dromara.manager.driver.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.excel.utils.ExcelUtil;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.web.core.BaseController;
import org.dromara.manager.driver.domain.bo.DriverApplicationBo;
import org.dromara.manager.driver.domain.vo.DriverApplicationVo;
import org.dromara.manager.driver.service.IDriverApplicationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 驱动应用
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driverApplication")
public class DriverApplicationController extends BaseController {

    private final IDriverApplicationService driverApplicationService;

    /**
     * 查询驱动应用列表
     */
    @SaCheckPermission("manager:driverApplication:list")
    @GetMapping("/list")
    public TableDataInfo<DriverApplicationVo> list(DriverApplicationBo bo, PageQuery pageQuery) {
        return driverApplicationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出驱动应用列表
     */
    @SaCheckPermission("manager:driverApplication:export")
    @Log(title = "驱动应用", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DriverApplicationBo bo, HttpServletResponse response) {
        List<DriverApplicationVo> list = driverApplicationService.queryList(bo);
        ExcelUtil.exportExcel(list, "驱动应用", DriverApplicationVo.class, response);
    }

    /**
     * 获取驱动应用详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driverApplication:query")
    @GetMapping("/{id}")
    public R<DriverApplicationVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(driverApplicationService.queryById(id));
    }

//    /**
//     * 新增驱动应用
//     */
//    @SaCheckPermission("manager:driverApplication:add")
//    @Log(title = "驱动应用", businessType = BusinessType.INSERT)
//    @RepeatSubmit()
//    @PostMapping()
//    public R<Void> add(@Validated(AddGroup.class) @RequestBody DriverApplicationBo bo) {
//        return toAjax(driverApplicationService.insertByBo(bo));
//    }
//
//    /**
//     * 修改驱动应用
//     */
//    @SaCheckPermission("manager:driverApplication:edit")
//    @Log(title = "驱动应用", businessType = BusinessType.UPDATE)
//    @RepeatSubmit()
//    @PutMapping()
//    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DriverApplicationBo bo) {
//        return toAjax(driverApplicationService.updateByBo(bo));
//    }

    /**
     * 删除驱动应用
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:driverApplication:remove")
    @Log(title = "驱动应用", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(driverApplicationService.deleteWithValidByIds(List.of(ids), true));
    }
}
