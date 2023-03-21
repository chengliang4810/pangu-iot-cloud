package com.pangu.iot.manager.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.iot.manager.device.domain.GatewayDeviceBind;
import com.pangu.iot.manager.device.mapper.GatewayDeviceMapper;
import com.pangu.iot.manager.device.service.IGatewayDeviceBindService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GatewayDeviceBindServiceImpl extends ServiceImpl<GatewayDeviceMapper, GatewayDeviceBind> implements IGatewayDeviceBindService {
}
