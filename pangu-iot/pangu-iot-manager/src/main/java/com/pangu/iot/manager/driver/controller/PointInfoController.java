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
import com.pangu.iot.manager.driver.domain.bo.PointInfoBO;
import com.pangu.iot.manager.driver.domain.bo.PointInfoBatchBO;
import com.pangu.iot.manager.driver.domain.vo.PointInfoVO;
import com.pangu.iot.manager.driver.service.IPointInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 点位属性配置信息控制器
 * 前端访问路由地址为:/manager/driver/point/info
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/driver/point/info")
public class PointInfoController extends BaseController {

    private final IPointInfoService pointInfoService;

    /**
     * 新增点位属性配置信息
     */
    @SaCheckPermission("manager:driver/point/info:add")
    @Log(title = "点位属性配置信息", businessType = BusinessType.INSERT)
    @PostMapping("/batch")
    public R<Void> addBatch(@Validated(AddGroup.class) @RequestBody PointInfoBatchBO bo) {
        return toAjax(pointInfoService.batchUpdate(bo));
    }

    /**
     * 查询点位属性配置信息列表
     */
    @SaCheckPermission("manager:driver/point/info:list")
    @GetMapping("/list")
    public TableDataInfo<PointInfoVO> list(PointInfoBO bo, PageQuery pageQuery) {
        return pointInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出点位属性配置信息列表
     */
    @SaCheckPermission("manager:driver/point/info:export")
    @Log(title = "点位属性配置信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(PointInfoBO bo, HttpServletResponse response) {
        List<PointInfoVO> list = pointInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "点位属性配置信息", PointInfoVO.class, response);
    }

    /**
     * 获取点位属性配置信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("manager:driver/point/info:query")
    @GetMapping("/{id}")
    public R<PointInfoVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(pointInfoService.queryById(id));
    }

    /**
     * 新增点位属性配置信息
     */
    @SaCheckPermission("manager:driver/point/info:add")
    @Log(title = "点位属性配置信息", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody PointInfoBO bo) {
        return toAjax(pointInfoService.insertByBo(bo));
    }

    /**
     * 修改点位属性配置信息
     */
    @SaCheckPermission("manager:driver/point/info:edit")
    @Log(title = "点位属性配置信息", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody PointInfoBO bo) {
        return toAjax(pointInfoService.updateByBo(bo));
    }

    /**
     * 删除点位属性配置信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("manager:driver/point/info:remove")
    @Log(title = "点位属性配置信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(pointInfoService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
