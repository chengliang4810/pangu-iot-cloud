package com.pangu.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.StrUtil;
import com.pangu.common.core.domain.R;
import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.controller.BaseController;
import com.pangu.common.excel.utils.ExcelUtil;
import com.pangu.common.log.annotation.Log;
import com.pangu.common.log.enums.BusinessType;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.system.domain.bo.ApiTokenBO;
import com.pangu.system.domain.vo.ApiTokenVO;
import com.pangu.system.service.IApiTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 三方授权控制器
 * 前端访问路由地址为:/system/token
 *
 * @author chengliang4810
 * @date 2023-03-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/token")
public class ApiTokenController extends BaseController {

    private final IApiTokenService apiTokenService;

    /**
     * 查询三方授权列表
     */
    @SaCheckPermission("system:token:list")
    @GetMapping("/list")
    public TableDataInfo<ApiTokenVO> list(ApiTokenBO bo, PageQuery pageQuery) {
        return apiTokenService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出三方授权列表
     */
    @SaCheckPermission("system:token:export")
    @Log(title = "三方授权", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(ApiTokenBO bo, HttpServletResponse response) {
        List<ApiTokenVO> list = apiTokenService.queryList(bo);
        ExcelUtil.exportExcel(list, "三方授权", ApiTokenVO.class, response);
    }

    /**
     * 获取三方授权详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:token:query")
    @GetMapping("/{id}")
    public R<ApiTokenVO> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(apiTokenService.queryById(id));
    }

    /**
     * 新增三方授权
     */
    @SaCheckPermission("system:token:add")
    @Log(title = "三方授权", businessType = BusinessType.INSERT)
    @PostMapping()
    public R<String> add(@Validated(AddGroup.class) @RequestBody ApiTokenBO bo) {
        String token = apiTokenService.insertByBo(bo);
        if (StrUtil.isBlank(token)) {
            return R.fail("创建失败");
        }
        return R.ok("创建成功", token);
    }

    /**
     * 修改三方授权
     */
    @SaCheckPermission("system:token:edit")
    @Log(title = "三方授权", businessType = BusinessType.UPDATE)
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ApiTokenBO bo) {
        return toAjax(apiTokenService.updateByBo(bo));
    }

    /**
     * 修改三方授权
     */
    @SaCheckPermission("system:token:edit")
    @Log(title = "三方授权", businessType = BusinessType.UPDATE)
    @PutMapping("/reset/{id}")
    public R<String> reset(@PathVariable Long id) {
        String token = apiTokenService.resetToken(id);
        if (StrUtil.isBlank(token)) {
            return R.fail("创建失败");
        }
        return R.ok("创建成功", token);
    }

    /**
     * 删除三方授权
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:token:remove")
    @Log(title = "三方授权", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(apiTokenService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
