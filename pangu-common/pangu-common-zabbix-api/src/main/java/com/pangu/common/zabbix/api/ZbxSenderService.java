package com.pangu.common.zabbix.api;

import cn.hutool.core.util.NumberUtil;
import com.dtflys.forest.config.ForestConfiguration;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.model.ZbxResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * zbx发送服务
 *
 * @author zeus-iot
 * @date 2023/01/09 10:47
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ZbxSenderService {


    private final ForestConfiguration forestConfiguration;

    /**
     * 指定格式的 JSON
     *
     * @param message see
     *                https://www.zabbix.com/documentation/current/manual/appendix/items/trapper
     *
     * {
     * 	"request":"sender data",
     * 	"data":[
     *      {
     *          "host":"device.info",
     *          "key":"device.temp",
     * 	        "value":"86"
     *      }
     * 	]
     * }
     * @return
     * @throws IOException ex
     */
    public ZbxResponse sendData(String message) throws IOException {

        Socket trapperSocket = this.getSocket();
        log.debug("发送数据:{}", message);

        int payloadLength = length(message);
        byte[] header = new byte[]{
                'Z', 'B', 'X', 'D', '\1',
                (byte) (payloadLength & 0xFF),
                (byte) ((payloadLength >> 8) & 0xFF),
                (byte) ((payloadLength >> 16) & 0xFF),
                (byte) ((payloadLength >> 24) & 0xFF),
                '\0', '\0', '\0', '\0'};

        ByteBuffer byteBuffer = ByteBuffer.allocate(header.length + payloadLength);
        byteBuffer.put(header);
        byteBuffer.put(message.getBytes(StandardCharsets.UTF_8));

        byte[] response = new byte[2048];

        OutputStream reqStream = trapperSocket.getOutputStream();
        reqStream.write(byteBuffer.array());
        reqStream.flush();

        byteBuffer = null;

        InputStream resStream = trapperSocket.getInputStream();
        StringBuilder resp = new StringBuilder();

        int headLength = 13;
        int bRead = 0;

        while (true) {
            bRead = resStream.read(response);
            if (bRead <= 0) {
                break;
            }
            resp.append(new String(Arrays.copyOfRange(response, headLength, bRead)));
            headLength = 0;
        }

        log.debug(" Zabbix Trapper 响应数据：{} ", resp.toString());

        resStream.close();
        reqStream.close();
        trapperSocket.close();
        trapperSocket = null;

        return JsonUtils.parseObject(zabbixResponseToMap(resp.toString()), ZbxResponse.class);
    }


    /**
     * zabbix 返回字符串 处理
     *
     * @param resp
     * @return String
     */
    private String zabbixResponseToMap(String resp) {
        Map<String, String> result = JsonUtils.parseObject(resp, Map.class);

        String info = result.get("info");
        if (info == null) {
            return resp;
        }

        String[] infos = info.split(";");

        Map<String, String> resultMap = new HashMap<>();
        for (String i : infos) {
            String[] ii = i.split(":");
            resultMap.put(ii[0].trim(), ii[1].trim());
        }

        resultMap.put("response", result.get("response"));
        // String {"processed":"1","total":"1","seconds spent":" 0.000020","response":"success","failed":" 0"}
        return JsonUtils.toJsonString(resultMap);
    }

    /**
     * 计算字符串长度 中文3个字节
     * @param value
     * @return
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 3;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }


    /**
     * 获取 Socket 实例
     *
     * @return Socket
     */
    private Socket getSocket() {
        Socket trapperSocket = new Socket();
        Object zbxServerIp = forestConfiguration.getVariableValue("zbxServerIp");
        Object zbxSenderPort = forestConfiguration.getVariableValue("zbxSenderPort");
        try {
            trapperSocket.setSoTimeout(1000);
            Assert.notNull(zbxServerIp, "zbxServerIp is null");
            Assert.notNull(zbxSenderPort, "zbxSenderPort is null");
            log.debug("zbxServerIp:{}, zbxSenderPort:{}", zbxServerIp, zbxSenderPort);
            trapperSocket.connect(new InetSocketAddress(zbxServerIp.toString(), NumberUtil.parseInt(zbxSenderPort.toString())));
        } catch (Exception e) {
            log.error("connect zabbix socket fail [ip:{}, port: {}] : {}", zbxServerIp, zbxSenderPort, e.getMessage());
        }
        return trapperSocket;
    }

}
