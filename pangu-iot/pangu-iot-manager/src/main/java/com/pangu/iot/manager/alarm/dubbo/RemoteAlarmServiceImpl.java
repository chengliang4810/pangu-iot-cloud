package com.pangu.iot.manager.alarm.dubbo;

import com.pangu.iot.manager.alarm.convert.ProblemConvert;
import com.pangu.iot.manager.alarm.service.IProblemService;
import com.pangu.iot.manager.product.service.IProductEventService;
import com.pangu.manager.api.RemoteAlarmService;
import com.pangu.manager.api.model.AlarmDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteAlarmServiceImpl implements RemoteAlarmService {


    private final IProblemService problemService;
    private final ProblemConvert problemConvert;
    private final IProductEventService productEventService;

    /**
     * 添加报警记录
     *
     * @param alarmDTO 报警dto
     */
    @Override
    public void addAlarmRecord(AlarmDTO alarmDTO) {
        log.info("addAlarmRecord:{}", alarmDTO);

    }

}

