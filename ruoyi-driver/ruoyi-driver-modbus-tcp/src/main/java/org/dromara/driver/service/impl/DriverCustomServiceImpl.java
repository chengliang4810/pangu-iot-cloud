package org.dromara.driver.service.impl;

import com.graphbuilder.curve.Point;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.iot.entity.driver.AttributeInfo;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.sdk.service.DriverCustomService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class DriverCustomServiceImpl implements DriverCustomService {

    /**
     * 初始化接口，会在驱动启动时执行
     */
    @Override
    public void initial() {

    }

    /**
     * 自定义调度接口，配置文件 driver.schedule.custom 进行配置
     */
    @Override
    public void schedule() {

    }

    /**
     * 读操作，请灵活运行，有些类型设备不一定能直接读取数据
     *
     * @param driverInfo Driver Attribute Info
     * @param pointInfo  Point Attribute Info
     * @param device     Device
     * @param point      Point
     * @return String Value
     */
    @Override
    public String read(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, Point point) {
        return null;
    }

    /**
     * 写操作，请灵活运行，有些类型设备不一定能直接写入数据
     *
     * @param driverInfo Driver Attribute Info
     * @param pointInfo  Point Attribute Info
     * @param device     Device
     * @param value      Value Attribute Info
     * @return Boolean 是否写入
     */
    @Override
    public Boolean write(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, AttributeInfo value) {
        return null;
    }

}
