package com.pangu.iot.manager.device.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.bo.ServiceExecuteRecordBO;
import com.pangu.iot.manager.device.domain.vo.ServiceExecuteRecordVO;
import com.pangu.iot.manager.device.service.IServiceExecuteRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/device/log")
public class DeviceLogController {

    private final IServiceExecuteRecordService serviceExecuteRecordService;

    /**
     * 查询功能执行记录列表
     */
    @GetMapping("/service/list")
    @SaCheckPermission("manager:service_execute_record:list")
    public TableDataInfo<ServiceExecuteRecordVO> list(ServiceExecuteRecordBO bo, PageQuery pageQuery) {
        return serviceExecuteRecordService.queryPageList(bo, pageQuery);
    }

}
