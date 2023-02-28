package com.pangu.iot.driver.service;

import cn.hutool.core.util.RandomUtil;
import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceFunction;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.iot.driver.ValueConstant;
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
import javassist.bytecode.AttributeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DefaultDriverDataService implements DriverDataService {

    static ModbusFactory modbusFactory;

    static {
        modbusFactory = new ModbusFactory();
    }

    private volatile Map<String, ModbusMaster> masterMap = new HashMap<>(64);

    @Override
    public List<DeviceValue> read() {
        DeviceValue deviceValue = new DeviceValue();
        deviceValue.setDeviceId("1623590722222985216");
        deviceValue.setAttributes(Collections.singletonMap("temp", String.valueOf(RandomUtil.randomInt(1, 100))));
        deviceValue.setClock(System.currentTimeMillis() / 1000);
        return Collections.singletonList(deviceValue);
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
            params.setHost("host");
            params.setPort(8888);
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
        int slaveId = 0;//attribute(pointInfo, "slaveId");
        int functionCode = 0;//attribute(pointInfo, "functionCode");
        int offset = 0;//attribute(pointInfo, "offset");
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
        int slaveId = 0;// attribute(pointInfo, "slaveId");
        int functionCode = 0;//attribute(pointInfo, "functionCode");
        int offset = 0;//attribute(pointInfo, "offset");
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
        switch (type.toLowerCase()) {
            case ValueConstant.Type.LONG:
                return DataType.FOUR_BYTE_INT_SIGNED;
            case ValueConstant.Type.FLOAT:
                return DataType.FOUR_BYTE_FLOAT;
            case ValueConstant.Type.DOUBLE:
                return DataType.EIGHT_BYTE_FLOAT;
            default:
                return DataType.TWO_BYTE_INT_SIGNED;
        }
    }

}
