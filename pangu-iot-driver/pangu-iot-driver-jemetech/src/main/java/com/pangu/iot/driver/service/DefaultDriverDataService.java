package com.pangu.iot.driver.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import com.pangu.common.core.domain.dto.AttributeInfo;
import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceFunction;
import com.pangu.common.zabbix.model.DeviceValue;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.pangu.common.sdk.utils.DriverUtil.attribute;

@Slf4j
@Component
public class DefaultDriverDataService extends DriverDataService {

    /**
     * 套接字
     */
    private final Socket socket = new Socket();

    /**
     * 输入流
     */
    private InputStream inputStream;

    /**
     * 数据长度
     */
    private final byte[] dataLengthBytes = new byte[4];

    /**
     * 数据格式化
     */
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.00000000");

    @Override
    public DeviceValue read(Device device, List<DeviceAttribute> attributes) throws Exception {
        return super.read(device, attributes);
    }

    @Override
    @SneakyThrows
    public String read(Device device, DeviceAttribute attribute, Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo) {

        // 访问服务器
        connect(attribute(driverInfo, "host"), attribute(driverInfo, "port"));

        byte[] bytes = null;
        try {
            // 获取数据长度
            inputStream.read(dataLengthBytes);
            int dataLength = getDataLength(dataLengthBytes);
            log.debug("dataLength: {}", dataLength);
            // 根据数据长度构建字节数组
            bytes = new byte[dataLength];
            // 读取数据
            inputStream.read(bytes);
        } catch (SocketException exception) {
            log.error("连接失败，重新连接！", exception);
            connect(attribute(driverInfo, "host"), attribute(driverInfo, "port"));
        }
        // 16进制字符串
        String dataString = Convert.toHex(bytes);
        // 获取配置的传感器ID
        Integer sensorId = attribute(pointInfo, "sensorId");
        // 读取指定传感器ID数据
        Float floatValue = getSensorData(driverInfo,dataString, sensorId);
        log.debug("floatValue: {}", floatValue);
        if (floatValue == null) {
            // 设备故障，无法读取数据
            throw new ServiceException("设备 [" + device.getId() + "] 故障，无法读取数据");
        }
        // 格式化数据
        return decimalFormat.format(floatValue);
    }

    private void closeRes() {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                log.error("关闭输入流失败", e);
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            log.error("关闭Socket", e);
        }
    }

    private void connect(String host, int port) {
        if (socket.isConnected()) {
            return;
        }
        InetSocketAddress mSocketAddress = new InetSocketAddress(host, port);
        try {
            socket.connect(mSocketAddress, 10000);// 设置连接超时时间为10秒
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            log.debug("连接失败: {}", e.getLocalizedMessage());
            // 让所有设备离线
            throw new ServiceException("连接失败");
        }
        log.debug("连接成功 host: {}, port: {}", host, port);
    }

    /**
     * 获取数据长度
     *
     * @param bytes 字节
     * @return int
     */
    private int getDataLength(byte[] bytes) {
        // 将字节数组转换为16进制字符串
        String hexString = Convert.toHex(bytes);
        // 截取字符串
        String dataLengthStr = hexString.substring(2, 4) + hexString.substring(0, 2);
        // 将16进制字符串转换十进制
        return Integer.parseInt(dataLengthStr, 16);
    }


    private Float getSensorData(Map<String, AttributeInfo> driverInfo, String dataString, Integer sensorId) {
        // 获取传感器数量
        int number = Integer.parseInt(dataString.substring(2, 4) + dataString.substring(0, 2), 16);
        log.debug("传感器数量: {}", number);
        if (number == 0) {
            closeRes();
            ThreadUtil.sleep(500);
            log.warn("传感器数量为0，重新连接");
            connect(attribute(driverInfo, "host"), attribute(driverInfo, "port"));
        }
        System.out.println("传感器ID start");
        for (int i = 0; i < number; i++) {
            // 获取传感器数据起始位置
            int startIndex = 40 * i + 4;
            Integer id = Integer.parseInt(dataString.substring(startIndex + 6, startIndex + 8) + dataString.substring(startIndex + 4, startIndex + 6), 16);
            // log.debug("序号 {} ,传感器id: {}", i + 1, id);
            System.out.println(id + ",");
        }
        System.out.println("传感器ID END");
        for (int i = 0; i < number; i++) {
            // 获取传感器数据起始位置
            int startIndex = 40 * i + 4;
            // 传感器id
            Integer id = Integer.parseInt(dataString.substring(startIndex + 6, startIndex + 8) + dataString.substring(startIndex + 4, startIndex + 6), 16);
            log.info("序号 {} ,传感器id: {}", i + 1, id);
            if (!Objects.equals(sensorId, id)) {
                continue;
            }
            // 截取传感器数据
            String sensorDataStr = dataString.substring(startIndex + 32, startIndex + 40);
            // 将16进制字符串转换十进制
            byte[] hexToBytes = Convert.hexToBytes(sensorDataStr);
            // 将byte进行little-endian转换
            float value = ByteBuffer.wrap(hexToBytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
            if (Float.isNaN(value)) {
                return null;
            } else {
                return BigDecimal.valueOf(value).setScale(8, RoundingMode.HALF_UP).floatValue();
            }
        }
        throw new ServiceException("传感器 [" + sensorId + "] 不存在");
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

}
