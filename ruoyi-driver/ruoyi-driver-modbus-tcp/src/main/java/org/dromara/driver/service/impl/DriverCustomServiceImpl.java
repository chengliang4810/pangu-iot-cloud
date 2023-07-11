package org.dromara.driver.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.iot.entity.device.DeviceStatus;
import org.dromara.common.iot.entity.driver.AttributeInfo;
import org.dromara.common.iot.entity.driver.Device;
import org.dromara.common.iot.enums.PointTypeFlagEnum;
import org.dromara.common.iot.model.Point;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.common.sdk.service.DriverCustomService;
import org.dromara.common.sdk.utils.DriverUtil;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.dromara.common.sdk.utils.DriverUtil.attribute;

@Slf4j
@Service
public class DriverCustomServiceImpl implements DriverCustomService {

    static ModbusFactory modbusFactory;

    static {
        modbusFactory = new ModbusFactory();
    }

    /**
     * 获取连接
     * deviceId, ModbusMaster链接对象
     */
    private Map<Long, ModbusMaster> connectMap;

    /**
     * 初始化接口，会在驱动启动时执行
     */
    @Override
    public void initial() {
        connectMap = new ConcurrentHashMap<>(16);
    }

    /**
     * 网关状态
     *
     * @param driverInfo driver info
     * @param device     设备
     * @return {@link DeviceStatus}
     */
    @Override
    public DeviceStatus gatewayStatus(Map<String, AttributeInfo> driverInfo, Device device) {
        try {
            getConnector(device.getDeviceId(), driverInfo);
            return DeviceStatus.online(device.getDeviceId());
        } catch (Exception e){
            log.warn("网关设备状态异常", e);
            return DeviceStatus.offline(device.getDeviceId());
        }
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
        ModbusMaster modbusMaster = getConnector(device.getDeviceId(), driverInfo);
        return readValue(modbusMaster, pointInfo, point.getAttributeType());
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
        ModbusMaster modbusMaster = getConnector(device.getDeviceId(), driverInfo);
        return writeValue(modbusMaster, pointInfo, value);
    }

    /**
     * 获取 Modbus Master
     *
     * @param deviceId   设备ID
     * @param driverInfo 驱动信息
     * @return ModbusMaster
     */
    private ModbusMaster getConnector(Long deviceId, Map<String, AttributeInfo> driverInfo) {
        log.debug("Modbus Tcp Connection Info: {}", JsonUtils.toJsonString(driverInfo));
        ModbusMaster modbusMaster = connectMap.get(deviceId);
        if (ObjUtil.isNull(modbusMaster) || !modbusMaster.isConnected()) {
            IpParameters params = new IpParameters();
            params.setHost(attribute(driverInfo, "host"));
            params.setPort(attribute(driverInfo, "port"));
            modbusMaster = modbusFactory.createTcpMaster(params, true);
            try {
                modbusMaster.init();
                connectMap.put(deviceId, modbusMaster);
            } catch (ModbusInitException e) {
                connectMap.entrySet().removeIf(next -> next.getKey().equals(deviceId));
                log.error("Connect modbus master error: {}", e.getMessage(), e);
                throw new ServiceException(e.getMessage());
            }
        }
        return modbusMaster;
    }

    /**
     * 获取 Value
     *
     * @param modbusMaster ModbusMaster
     * @param pointInfo    Point Attribute Config
     * @param type         Value Type
     * @return String Value
     */
    private String readValue(ModbusMaster modbusMaster, Map<String, AttributeInfo> pointInfo, String type) {
        int slaveId = attribute(pointInfo, "slaveId");
        int functionCode = attribute(pointInfo, "functionCode");
        int offset = attribute(pointInfo, "offset");
        switch (functionCode) {
            case 1:
                BaseLocator<Boolean> coilLocator = BaseLocator.coilStatus(slaveId, offset);
                Boolean coilValue = getMasterValue(modbusMaster, coilLocator);
                return String.valueOf(coilValue);
            case 2:
                BaseLocator<Boolean> inputLocator = BaseLocator.inputStatus(slaveId, offset);
                Boolean inputStatusValue = getMasterValue(modbusMaster, inputLocator);
                return String.valueOf(inputStatusValue);
            case 3:
                BaseLocator<Number> holdingLocator = BaseLocator.holdingRegister(slaveId, offset, getValueType(type));
                Number holdingValue = getMasterValue(modbusMaster, holdingLocator);
                return String.valueOf(holdingValue);
            case 4:
                BaseLocator<Number> inputRegister = BaseLocator.inputRegister(slaveId, offset, getValueType(type));
                Number inputRegisterValue = getMasterValue(modbusMaster, inputRegister);
                return String.valueOf(inputRegisterValue);
            default:
                return "0";
        }
    }

    /**
     * 获取 ModbusMaster 值
     *
     * @param modbusMaster ModbusMaster
     * @param locator      BaseLocator
     * @param <T>          类型
     * @return 类型
     */
    private <T> T getMasterValue(ModbusMaster modbusMaster, BaseLocator<T> locator) {
        try {
            return modbusMaster.getValue(locator);
        } catch (ModbusTransportException | ErrorResponseException e) {
            log.error("Read modbus master value error: {}", e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 写入 ModbusMaster 值
     *
     * @param modbusMaster ModbusMaster
     * @param pointInfo    Point Attribute Config
     * @param value        Value
     * @return Write Result
     */
    private boolean writeValue(ModbusMaster modbusMaster, Map<String, AttributeInfo> pointInfo, AttributeInfo value) {
        int slaveId = attribute(pointInfo, "slaveId");
        int functionCode = attribute(pointInfo, "functionCode");
        int offset = attribute(pointInfo, "offset");
        switch (functionCode) {
            case 1:
                WriteCoilResponse coilResponse = setMasterValue(modbusMaster, slaveId, offset, value);
                return !coilResponse.isException();
            case 3:
                BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId, offset, getValueType(value.getType().getCode()));
                setMasterValue(modbusMaster, locator, value);
                return true;
            default:
                return false;
        }
    }

    /**
     * 获取数据类型
     * 说明：此处可根据实际项目情况进行拓展
     * 1.swap 交换
     * 2.大端/小端,默认是大端
     * 3.拓展其他数据类型
     *
     * @param type Value Type
     * @return Modbus Data Type
     */
    private int getValueType(String type) {
        PointTypeFlagEnum valueType = PointTypeFlagEnum.ofCode(type);
        if (ObjectUtil.isNull(valueType)) {
            throw new IllegalArgumentException("Unsupported type of " + type);
        }

        switch (valueType) {
            case LONG:
                return DataType.FOUR_BYTE_INT_SIGNED;
            case FLOAT:
                return DataType.FOUR_BYTE_FLOAT;
            case DOUBLE:
                return DataType.EIGHT_BYTE_FLOAT;
            default:
                return DataType.TWO_BYTE_INT_SIGNED;
        }
    }

    /**
     * 写入 ModbusMaster 值
     *
     * @param modbusMaster ModbusMaster
     * @param slaveId      从站ID
     * @param offset       偏移量
     * @param value        写入值
     * @return WriteCoilResponse
     */
    private WriteCoilResponse setMasterValue(ModbusMaster modbusMaster, int slaveId, int offset, AttributeInfo value) {
        try {
            WriteCoilRequest coilRequest = new WriteCoilRequest(slaveId, offset, DriverUtil.value(value.getType().getCode(), value.getValue()));
            return (WriteCoilResponse) modbusMaster.send(coilRequest);
        } catch (ModbusTransportException e) {
            log.error("Write modbus master value error: {}", e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 写入 ModbusMaster 值
     *
     * @param modbusMaster ModbusMaster
     * @param locator      BaseLocator
     * @param value        写入值
     * @param <T>          类型
     */
    private <T> void setMasterValue(ModbusMaster modbusMaster, BaseLocator<T> locator, AttributeInfo value) {
        try {
            modbusMaster.setValue(locator, DriverUtil.value(value.getType().getCode(), value.getValue()));
        } catch (ModbusTransportException | ErrorResponseException e) {
            log.error("Write modbus master value error: {}", e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

}
