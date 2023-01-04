package com.ruoyi.common.core.web.controller;

import com.ruoyi.common.core.domain.R;
import lombok.extern.slf4j.Slf4j;

/**
 * web层通用数据处理
 *
 * @author Lion Li
 */
@Slf4j
public class BaseController {

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected R<Void> toAjax(int rows) {
        return rows > 0 ? R.ok() : R.fail();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected R<Void> toAjax(boolean result) {
        return result ? R.ok() : R.fail();
    }

}
