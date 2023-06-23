package org.dromara.common.sdk.service;


import com.graphbuilder.curve.Point;
import org.dromara.common.iot.entity.driver.AttributeInfo;
import org.dromara.common.iot.entity.driver.Device;

import java.util.Map;


/**
 * 自定义驱动接口，开发的自定义驱动至少需要实现 read 和 write 接口，可以参考以提供的驱动模块写法
 *
 * @author pnoker
 * @since 2022.1.0
 */
public interface DriverCustomService {
    /**
     * 初始化接口，会在驱动启动时执行
     */
    void initial();

    /**
     * 自定义调度接口，配置文件 driver.schedule.custom 进行配置
     */
    void schedule();

    /**
     * 读操作，请灵活运行，有些类型设备不一定能直接读取数据
     *
     * @param driverInfo Driver Attribute Info
     * @param pointInfo  Point Attribute Info
     * @param device     Device
     * @param point      Point
     * @return String Value
     */
    String read(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, Point point);

    /**
     * 写操作，请灵活运行，有些类型设备不一定能直接写入数据
     *
     * @param driverInfo Driver Attribute Info
     * @param pointInfo  Point Attribute Info
     * @param device     Device
     * @param value      Value Attribute Info
     * @return Boolean 是否写入
     */
    Boolean write(Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo, Device device, AttributeInfo value);

}
