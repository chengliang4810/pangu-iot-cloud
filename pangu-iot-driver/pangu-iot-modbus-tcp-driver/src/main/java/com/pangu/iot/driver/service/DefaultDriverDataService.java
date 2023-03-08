package com.pangu.iot.driver.service;

import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceFunction;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.manager.api.domain.AttributeInfo;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
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
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pangu.common.sdk.utils.DriverUtil.attribute;

@Slf4j
@Component
public class DefaultDriverDataService extends DriverDataService {

    static ModbusFactory modbusFactory;

    static {
        modbusFactory = new ModbusFactory();
    }

    private volatile Map<String, ModbusMaster> masterMap = new HashMap<>(64);

    @Override
    public DeviceValue read(Device device, List<DeviceAttribute> attributes) {
        // 驱动信息
        Map<String, AttributeInfo> driverInfo = driverContext.getDriverInfoByDeviceId(device.getId());
        log.info("Driver Info: {}", JsonUtils.toJsonString(driverInfo));

        Map<String, String> attributesMap = attributes.parallelStream().collect(Collectors.toMap(DeviceAttribute::getKey, attribute -> {
            Map<String, AttributeInfo> pointInfo = driverContext.getPointInfoByDeviceIdAndPointId(device.getId(), attribute.getId());
            log.info("Point Info: {}", JsonUtils.toJsonString(pointInfo));

            try {
                ModbusMaster master = getMaster(device.getId().toString(), driverInfo);
                return readValue(master, pointInfo, attribute.getValueType());
            } catch (ModbusInitException | ModbusTransportException | ErrorResponseException e) {
                throw new RuntimeException(e);
            }
        }));

        return new DeviceValue(device.getCode(), attributesMap);
    }

    /**
     * 控制设备
     *
     * @param deviceFunction
     */
    @Override
    public Boolean control(DeviceFunction deviceFunction) {
        // 通过异常信息记录日志
        throw new ServiceException("暂不支持控制设备");
        // return true;
    }


    /**
     * 获取 Modbus Master
     *
     * @param deviceId   Device Id
     * @param driverInfo Driver Info
     * @return ModbusMaster
     * @throws ModbusInitException ModbusInitException
     */
    public ModbusMaster getMaster(String deviceId, Map<String, AttributeInfo> driverInfo) throws ModbusInitException {
        log.debug("Modbus Tcp Connection Info {}", JsonUtils.toJsonString(driverInfo));
        ModbusMaster modbusMaster = masterMap.get(deviceId);
        if (null == modbusMaster) {
            IpParameters params = new IpParameters();
            params.setHost(attribute(driverInfo, "host"));
            params.setPort(attribute(driverInfo, "port"));
            modbusMaster = modbusFactory.createTcpMaster(params, true);
            modbusMaster.init();
            masterMap.put(deviceId, modbusMaster);
        }
        return modbusMaster;
    }



    /**
     * 获取 Value
     *
     * @param modbusMaster ModbusMaster
     * @param pointInfo    Point Info
     * @return String Value
     * @throws ModbusTransportException ModbusTransportException
     * @throws ErrorResponseException   ErrorResponseException
     */
    public String readValue(ModbusMaster modbusMaster, Map<String, AttributeInfo> pointInfo, String type) throws ModbusTransportException, ErrorResponseException {
        int slaveId = attribute(pointInfo, "slaveId");
        int functionCode = attribute(pointInfo, "functionCode");
        int offset = attribute(pointInfo, "offset");
        log.info("Modbus Tcp Read Value Info slaveId: {}, functionCode: {}, offset: {}", slaveId, functionCode, offset);
        switch (functionCode) {
            case 1:
                BaseLocator<Boolean> coilLocator = BaseLocator.coilStatus(slaveId, offset);
                Boolean coilValue = modbusMaster.getValue(coilLocator);
                return String.valueOf(coilValue);
            case 2:
                BaseLocator<Boolean> inputLocator = BaseLocator.inputStatus(slaveId, offset);
                Boolean inputStatusValue = modbusMaster.getValue(inputLocator);
                return String.valueOf(inputStatusValue);
            case 3:
                BaseLocator<Number> holdingLocator = BaseLocator.holdingRegister(slaveId, offset, getValueType(type));
                Number holdingValue = modbusMaster.getValue(holdingLocator);
                return String.valueOf(holdingValue);
            case 4:
                BaseLocator<Number> inputRegister = BaseLocator.inputRegister(slaveId, offset, getValueType(type));
                Number inputRegisterValue = modbusMaster.getValue(inputRegister);
                return String.valueOf(inputRegisterValue);
            default:
                return "0";
        }
    }

    /**
     * 写 Value
     *
     * @param modbusMaster ModbusMaster
     * @param pointInfo    Point Info
     * @param type         Value Type
     * @param value        String Value
     * @return Write Result
     * @throws ModbusTransportException ModbusTransportException
     * @throws ErrorResponseException   ErrorResponseException
     */
    public boolean writeValue(ModbusMaster modbusMaster, Map<String, AttributeInfo> pointInfo, String type, String value) throws ModbusTransportException, ErrorResponseException {
        int slaveId =  attribute(pointInfo, "slaveId");
        int functionCode = attribute(pointInfo, "functionCode");
        int offset = attribute(pointInfo, "offset");
        switch (functionCode) {
            case 1:
                boolean coilValue = false;//value(type, value);
                WriteCoilRequest coilRequest = new WriteCoilRequest(slaveId, offset, coilValue);
                WriteCoilResponse coilResponse = (WriteCoilResponse) modbusMaster.send(coilRequest);
                return !coilResponse.isException();
            case 3:
                BaseLocator<Number> locator = BaseLocator.holdingRegister(slaveId, offset, getValueType(type));
                modbusMaster.setValue(locator, ""); //value(type, value));
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
    public int getValueType(String type) {
        log.info("Modbus Tcp Value Type {}", type);
        switch (type) {
            case "0":
                return DataType.EIGHT_BYTE_FLOAT;
            case "1":
                return DataType.CHAR;
            case "4":
                return DataType.VARCHAR;
            default:
                return DataType.TWO_BYTE_INT_SIGNED;
        }
    }

}
