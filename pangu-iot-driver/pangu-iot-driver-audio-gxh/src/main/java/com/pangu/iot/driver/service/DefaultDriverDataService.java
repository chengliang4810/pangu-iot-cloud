package com.pangu.iot.driver.service;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.pangu.common.core.domain.dto.AttributeInfo;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceFunction;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.pangu.common.sdk.utils.DriverUtil.attribute;
import static com.pangu.common.sdk.utils.DriverUtil.value;

@Slf4j
@Component
public class DefaultDriverDataService extends DriverDataService {


    /**
     * Opc Da Server Map
     */
    private final Map<Long, String> tokenMap = new ConcurrentHashMap<>(64);


    /**
     * 读取设备单个属性数据
     *
     * @param device
     * @param attribute
     * @param driverInfo
     * @param pointInfo
     */
    @Override
    public String read(Device device, DeviceAttribute attribute, Map<String, AttributeInfo> driverInfo, Map<String, AttributeInfo> pointInfo) throws Exception {
        String token = getToken(device.getId(), driverInfo);

        // Token 获取不到，网络或者设备出问题
        if (CharSequenceUtil.isBlank(token)){
            return null;
        }
        int termID = -1;
        try {
             termID = attribute(pointInfo, "termID");
        } catch (Exception e){
            log.warn("获取设备{}属性{}失败 , termID 未配置", device.getId(), attribute.getName());
            return null;
        }
        // 构建查询参数
        Map<String, List<Object>> params = Collections.singletonMap("TermIDs", Collections.singletonList(termID));
        String url = getSiteUrl(driverInfo) + "/getTermState;JSESSIONID=" + token;
        // 查询设备状态
        String result= HttpUtil.post(url,  JSONUtil.toJsonStr(params));
        // 如果响应不是JSON，说明响应不正常
        if(!JSONUtil.isJson(result)){
            tokenMap.remove(device.getId());
            log.warn("读取设备{}信息失败, Response: {}", device.getId(), result);
            return null;
        }

        JSONObject jsonObject = JSONUtil.parseObj(result);

        if (jsonObject.get("Ret",Integer.class) != 0){
            tokenMap.remove(device.getId());
            log.warn("获取设备状态失败，设备ID：{}，错误信息：{}", device.getId(), jsonObject.get("Remark"));
            return null;
        }

        Optional<Object> term = jsonObject.getJSONArray("Terms").stream().findFirst();
        if (term.isPresent()){
            String jsonStr = JSONUtil.toJsonStr(term.get());
            log.debug("设备状态：{}", jsonStr);
            JSONObject jsonObject1 = JSONUtil.parseObj(jsonStr);
            // -1-离线 0-空闲 >0-工作中
            String status = jsonObject1.getStr("Status");
            if (StrUtil.equals(status, "0")){
                return "空闲";
            }else if (StrUtil.equals(status, "-1")){
                return "离线";
            }else {
                return "工作中";
            }
        }
        tokenMap.remove(device.getId());
        return null;
    }

//    public static void main(String[] args) {
//        // 构建URL
//        String url = "http://172.21.137.6/login";
//
//        // 构建登录参数
//        Map<String,Object> bodyMap = new HashMap<>(2);
//        bodyMap.put("User", "admin");
//        bodyMap.put("Passwd", "admin");
//
//        // 发出请求
//        String jsonBody = HttpUtil.post(url, JSONUtil.toJsonStr(bodyMap));
//        log.info("登录获取Token, Response: {}", jsonBody);
//        // 如果响应不是JSON，说明响应不正常
//        if(!JSONUtil.isJson(jsonBody)){
//            log.info("登录获取Token失败, Response: {}", jsonBody);
//            return;
//        }
//        // 解析结果
//        JSONObject jsonObject = JSONUtil.parseObj(jsonBody);
//        // 如果响应状态码不为（0）成功状态 或Token 不存在
//        if (jsonObject.get("Ret",Integer.class) != 0 || CharSequenceUtil.isBlank(jsonObject.get("JSessionID",String.class))){
//            log.info("登录获取Token失败, Response: {}", jsonBody);
//            return;
//        }
//
//        // 拿到Token 并存储
//        String token = jsonObject.getStr("JSessionID");
//
//        String post1 = HttpUtil.post("http://172.21.137.6/getTermIds;JSESSIONID=" + token, "");
//        log.info("获取设备列表：{}", post1);
//        JSONUtil.parseObj(post1).getJSONArray("TermIds").forEach(termID -> {
//            // 构建查询参数
//            Map<String, List<Object>> params = Collections.singletonMap("TermIDs", Collections.singletonList(termID));
//            String getUrl = "http://172.21.137.6/getTermState;JSESSIONID=" + token;
//            // 查询设备状态
//            String result= HttpUtil.post(getUrl,  JSONUtil.toJsonStr(params));
//            // 如果响应不是JSON，说明响应不正常
//            if(!JSONUtil.isJson(result)){
//                return;
//            }
//
//            JSONObject resultObject = JSONUtil.parseObj(result);
//
//            if (resultObject.get("Ret",Integer.class) != 0){
//                return;
//            }
//
//            Optional<Object> term = resultObject.getJSONArray("Terms").stream().findFirst();
//            if (term.isPresent()){
//                String jsonStr = JSONUtil.toJsonStr(term.get());
//                JSONObject entries = JSONUtil.parseObj(jsonStr);
//                System.out.println(entries.getStr("ID") + "\t" + entries.getStr("Name"));
//            }
//        });
//    }

    /**
     * 控制设备
     *
     * @param deviceFunction
     */
    @Override
    public Boolean control(DeviceFunction deviceFunction) {
        // 驱动信息
        Map<String, AttributeInfo> driverInfo = driverContext.getDriverInfoByDeviceId(deviceFunction.getDeviceId());
        Map<String, AttributeInfo> pointInfo = driverContext.getPointInfoByDeviceIdAndPointId(deviceFunction.getDeviceId(), deviceFunction.getServiceId());
        log.debug("Opc Da Write, device: {}, value: {}", JSON.toJSONString( deviceFunction), JSON.toJSONString(deviceFunction.getValue().getValue()));
        String message = value(deviceFunction.getValue().getType(), deviceFunction.getValue().getValue());
        String resultStr = new String(message.getBytes(), StandardCharsets.UTF_8);
        // 文本广播
        Map<String,Object> textPlayMap = new HashMap(16);
        textPlayMap.put("Content",  resultStr);
        // 0-分组 1-终端
        textPlayMap.put("TargetType", 1);
        // 目标ID列表
        textPlayMap.put("TargetIds", Collections.singletonList(attribute(pointInfo, "termID")));
        // 本条请求发生的时间点
        textPlayMap.put("Time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        //textPlayMap.put("TTSName","Microsoft Huihui Desktop - Chinese (Simplified)");

        // 1- GBK
        // 2- UTF-8
        textPlayMap.put("TextCode",2);
        // 提示音
        textPlayMap.put("PromptTone", 1);
        // 播放次数
        textPlayMap.put("Playtime", 1);
        // 音量
        textPlayMap.put("Volume", 56);
        // 播放间隔
        textPlayMap.put("PlayInterval", 1);
        // 播放优先级
        textPlayMap.put("PlayPrior", 999);
        // {"Content":"这是文本广播测试","Tids":[7],"Time":"20120212023621","Playtime":3,"PlayInterval":4,"PlayPrior":550}
        String jsonStr = JSONUtil.toJsonStr(textPlayMap);
        String url = getSiteUrl(driverInfo) + "/TextPlay;JSESSIONID=" + getToken(deviceFunction.getDeviceId(), driverInfo);
        String playResult = HttpUtil.post(url, jsonStr);

        if (!JSONUtil.isJson(playResult)){
            return false;
        }

        JSONObject jsonObject = JSONUtil.parseObj(playResult);
        if (jsonObject.get("Ret",Integer.class) != 0){
            log.warn("文本广播失败，设备ID：{}，错误信息：{}", deviceFunction.getDeviceId(), jsonObject.get("Remark"));
            return false;
        }

        ThreadUtil.execAsync(() -> {
            Object taskUUID = jsonObject.get("TaskUUID");
            String tempUrl = getSiteUrl(driverInfo) + "/TextPlayState;JSESSIONID=" + getToken(deviceFunction.getDeviceId(), driverInfo);
            for (int i = 0; i < 3; i++) {
                ThreadUtil.sleep(1000);
                String result = HttpUtil.post(tempUrl, Collections.singletonMap("TTSUUID", taskUUID));
                System.out.println(result);
                log.debug("文本广播结果：{}", result);
            }
        });

        log.info("文本广播成功，设备ID：{}，播放内容：{}, TaskUUID: {}", deviceFunction.getDeviceId(), message, jsonObject.get("TaskUUID"));
        return true;

    }


    /**
     * 获得令牌
     * 登录获取Token
     * @param deviceId   设备id
     * @param driverInfo 司机信息
     * @return {@link String}
     */
    private String getToken(Long deviceId, Map<String, AttributeInfo> driverInfo){
        String token = tokenMap.get(deviceId);

        // 存在Token则直接返回
        if (CharSequenceUtil.isNotBlank(token)){
            return token;
        }

        // 构建URL
        String url = getSiteUrl(driverInfo) + "/login";

        // 构建登录参数
        Map<String,Object> bodyMap = new HashMap<>(2);
        bodyMap.put("User", attribute(driverInfo, "username"));
        bodyMap.put("Passwd", attribute(driverInfo, "password"));

        // 发出请求
        String jsonBody = HttpUtil.post(url, JSONUtil.toJsonStr(bodyMap));
        log.debug("登录获取Token, Response: {}", jsonBody);
        // 如果响应不是JSON，说明响应不正常
        if(!JSONUtil.isJson(jsonBody)){
            return null;
        }
        // 解析结果
        JSONObject jsonObject = JSONUtil.parseObj(jsonBody);
        // 如果响应状态码不为（0）成功状态 或Token 不存在
        if (jsonObject.get("Ret",Integer.class) != 0 || CharSequenceUtil.isBlank(jsonObject.get("JSessionID",String.class))){
            return null;
        }

        // 拿到Token 并存储
        token = jsonObject.getStr("JSessionID");
        tokenMap.put(deviceId, token);
        return token;
    }

    /**
     * 网站网址
     * @param driverInfo 驱动信息
     * @return {@link String}
     */
    private String getSiteUrl(Map<String, AttributeInfo> driverInfo){
        String host = attribute(driverInfo, "host");
        int port = attribute(driverInfo, "port");
        return StrUtil.format("http://{}:{}", host, port);
    }


}
