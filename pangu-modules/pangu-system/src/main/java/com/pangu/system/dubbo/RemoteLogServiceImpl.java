package com.pangu.system.dubbo;

import com.pangu.system.api.RemoteLogService;
import com.pangu.system.api.domain.SysLogininfor;
import com.pangu.system.api.domain.SysOperLog;
import com.pangu.system.service.ISysLogininforService;
import com.pangu.system.service.ISysOperLogService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 操作日志记录
 *
 * @author chengliang4810
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteLogServiceImpl implements RemoteLogService {

    private final ISysOperLogService operLogService;
    private final ISysLogininforService logininforService;

    @Override
    public Boolean saveLog(SysOperLog sysOperLog) {
        return operLogService.insertOperlog(sysOperLog) > 0;
    }

    @Override
    public Boolean saveLogininfor(SysLogininfor sysLogininfor) {
        return logininforService.insertLogininfor(sysLogininfor) > 0;
    }
}
