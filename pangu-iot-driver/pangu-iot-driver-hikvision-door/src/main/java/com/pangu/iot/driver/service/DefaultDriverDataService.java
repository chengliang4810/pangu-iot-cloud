package com.pangu.iot.driver.service;

import Commom.osSelect;
import NetSDKDemo.HCNetSDK;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.pangu.common.core.domain.dto.AttributeInfo;
import com.pangu.common.core.exception.ServiceException;
import com.pangu.common.sdk.service.DriverDataService;
import com.pangu.common.zabbix.model.DeviceFunction;
import com.pangu.manager.api.domain.Device;
import com.pangu.manager.api.domain.DeviceAttribute;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.pangu.common.sdk.utils.DriverUtil.attribute;
import static com.pangu.common.sdk.utils.DriverUtil.value;

@Slf4j
@Component
public class DefaultDriverDataService extends DriverDataService {


    private static HCNetSDK hCNetSDK = null;
    public static int lAlarmHandle = -1; //布防句柄
    public static int lListenHandle = -1; //监听句柄
    static int iCharEncodeType = 0;  //设备字符集


    /**
     * 根据不同操作系统选择不同的库文件和库路径
     * @return
     */
    private static boolean CreateSDKInstance() {
        if (hCNetSDK == null) {
            synchronized (HCNetSDK.class) {
                String strDllPath = "";
                try {
                    if (osSelect.isWindows())
                    //win系统加载库路径
                    {
                        strDllPath = System.getProperty("user.dir") + "\\lib\\HCNetSDK.dll";
                    } else if (osSelect.isLinux())
                    //Linux系统加载库路径
                    {
                        // /dc3-driver/dc3-driver-opc-da/lib/libhcnetsdk.so
                        strDllPath = System.getProperty("user.dir") + "/lib/libhcnetsdk.so";
                    }
                    log.info("加载库文件路径：=========" + strDllPath);
                    log.info("加载库文件路径：{}", FileUtil.exist(strDllPath));

                    hCNetSDK = (HCNetSDK) Native.loadLibrary(strDllPath, HCNetSDK.class);
                } catch (Exception ex) {
                    log.error("loadLibrary: " + strDllPath + " Error: " + ex.getMessage());
                    return false;
                }
            }
        }
        return true;
    }


    public DefaultDriverDataService () {
        if (hCNetSDK == null && !CreateSDKInstance()) {
            log.error("初始化SDK失败");
            throw new ServiceException("初始化SDK失败");
        }

        //linux系统建议调用以下接口加载组件库
        if (osSelect.isLinux()) {
            HCNetSDK.BYTE_ARRAY ptrByteArray1 = new HCNetSDK.BYTE_ARRAY(256);
            HCNetSDK.BYTE_ARRAY ptrByteArray2 = new HCNetSDK.BYTE_ARRAY(256);
            //这里是库的绝对路径，请根据实际情况修改，注意改路径必须有访问权限
            String strPath1 = System.getProperty("user.dir") + "/lib/libcrypto.so.1.1";
            String strPath2 = System.getProperty("user.dir") + "/lib/libssl.so.1.1";
            System.arraycopy(strPath1.getBytes(), 0, ptrByteArray1.byValue, 0, strPath1.length());
            ptrByteArray1.write();
            hCNetSDK.NET_DVR_SetSDKInitCfg(3, ptrByteArray1.getPointer());
            System.arraycopy(strPath2.getBytes(), 0, ptrByteArray2.byValue, 0, strPath2.length());
            ptrByteArray2.write();
            hCNetSDK.NET_DVR_SetSDKInitCfg(4, ptrByteArray2.getPointer());
            String strPathCom = System.getProperty("user.dir") + "/lib";
            HCNetSDK.NET_DVR_LOCAL_SDK_PATH struComPath = new HCNetSDK.NET_DVR_LOCAL_SDK_PATH();
            System.arraycopy(strPathCom.getBytes(), 0, struComPath.sPath, 0, strPathCom.length());
            struComPath.write();
            hCNetSDK.NET_DVR_SetSDKInitCfg(2, struComPath.getPointer());
        }
        /**初始化*/
        hCNetSDK.NET_DVR_Init();
    }

    /**
     * 设备登录
     *
     * @param ipadress IP地址
     * @param user     用户名
     * @param psw      密码
     * @param port     端口，默认8000
     */
    public Integer login(String ipadress, String user, String psw, short port) {
        log.debug("登录信息：ipadress:{},user:{},psw:{},port:{}", ipadress, user, psw, port);
        //注册
        HCNetSDK.NET_DVR_USER_LOGIN_INFO m_strLoginInfo = new HCNetSDK.NET_DVR_USER_LOGIN_INFO();//设备登录信息
        String m_sDeviceIP = ipadress;//设备ip地址
        m_strLoginInfo.sDeviceAddress = new byte[HCNetSDK.NET_DVR_DEV_ADDRESS_MAX_LEN];
        System.arraycopy(m_sDeviceIP.getBytes(), 0, m_strLoginInfo.sDeviceAddress, 0, m_sDeviceIP.length());
        String m_sUsername = user;//设备用户名
        m_strLoginInfo.sUserName = new byte[HCNetSDK.NET_DVR_LOGIN_USERNAME_MAX_LEN];
        System.arraycopy(m_sUsername.getBytes(), 0, m_strLoginInfo.sUserName, 0, m_sUsername.length());
        String m_sPassword = psw;//设备密码
        m_strLoginInfo.sPassword = new byte[HCNetSDK.NET_DVR_LOGIN_PASSWD_MAX_LEN];
        System.arraycopy(m_sPassword.getBytes(), 0, m_strLoginInfo.sPassword, 0, m_sPassword.length());
        m_strLoginInfo.wPort = port; //sdk端口
        m_strLoginInfo.bUseAsynLogin = false; //是否异步登录：0- 否，1- 是
        m_strLoginInfo.write();
        HCNetSDK.NET_DVR_DEVICEINFO_V40 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V40();//设备信息
        Integer userId = hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
        if (userId == -1) {
            log.warn("Login failed, error code: " + hCNetSDK.NET_DVR_GetLastError());
            return userId;
        } else {
            log.debug("Login success, user id: " + userId);
            m_strDeviceInfo.read();
            iCharEncodeType = m_strDeviceInfo.byCharEncodeType;
            return userId;
        }
    }

    /**
     * 远程控门
     * @param lUserID 用户登录句柄
     * @param lGatewayIndex 门禁序号（楼层编号、锁ID），从1开始，-1表示对所有门（或者梯控的所有楼层）进行操作
     * @param dwStaic 命令值：0- 关闭（对于梯控，表示受控），1- 打开（对于梯控，表示开门），2- 常开（对于梯控，表示自由、通道状态），3- 常关（对于梯控，表示禁用），4- 恢复（梯控，普通状态），5- 访客呼梯（梯控），6- 住户呼梯（梯控）
     */
    public static boolean control(int lUserID, int lGatewayIndex, int dwStaic) {
        return hCNetSDK.NET_DVR_ControlGateway(lUserID, lGatewayIndex, dwStaic);
    }

    /**
     * Opc Da Server Map
     */
    private final Map<Long, Integer> userIdMap = new ConcurrentHashMap<>(64);


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
        log.debug("读取数据：driverInfo:{} , deviceId {}", JSONUtil.toJsonStr(driverInfo), device.getId());
        // 登录设备
        Integer userId = login(device.getId(), driverInfo);
        log.debug("设备[{}]登录成功，userId={}", device.getId(), userId);

        HCNetSDK.NET_DVR_ACS_WORK_STATUS_V50 netDvrAcsWorkStatusV50 = new HCNetSDK.NET_DVR_ACS_WORK_STATUS_V50();
        netDvrAcsWorkStatusV50.dwSize = netDvrAcsWorkStatusV50.size();
        netDvrAcsWorkStatusV50.write();

        // 获取门禁主机工作状态
        boolean b_GetAcsStatus = hCNetSDK.NET_DVR_GetDVRConfig(userId, HCNetSDK.NET_DVR_GET_ACS_WORK_STATUS_V50, 0xFFFFFFFF, netDvrAcsWorkStatusV50.getPointer(), netDvrAcsWorkStatusV50.size(), new IntByReference(0));
        if (!b_GetAcsStatus) {
            log.warn("获取门禁主机工作状态，错误码为：" + hCNetSDK.NET_DVR_GetLastError());
            throw new ServiceException("获取门禁主机工作状态失败");
        }

        // 读取
        netDvrAcsWorkStatusV50.read();
        Integer number = attribute(pointInfo, "number");
        log.debug("门号:{}， pointInfo： {}", number, pointInfo);
        if (number == null) {
            number = 1;
            log.warn("门编号为空， 使用默认值：1");
        }
        // 门磁状态 0-正常关，1-正常开，2-短路报警，3-断路报警，4-异常报警
        switch (netDvrAcsWorkStatusV50.byMagneticStatus[number-1] ){
            case 0:
                return "正常关";
            case 1:
                return "正常开";
            case 2:
                return "短路报警";
            case 3:
                return "断路报警";
            case 4:
                return "异常报警";
            default:
                return "正常关";
        }
    }

    private Integer login(Long deviceId, Map<String, AttributeInfo> driverInfo){
        Integer userId = userIdMap.get(deviceId);
        if (userId == null) {
            userId = login(attribute(driverInfo, "host"), attribute(driverInfo, "username"), attribute(driverInfo, "password"), attribute(driverInfo, "port"));
            if (userId == -1) {
                log.error("登录失败, driverInfo: {}", JSONUtil.toJsonStr(driverInfo));
                throw new ServiceException("登录失败");
            }
            userIdMap.put(deviceId, userId);
        }
        return userId;
    }

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

        log.info("设备{}，写入值：{}", deviceFunction.getDeviceId(), deviceFunction.getValue().getValue());
        Integer userId = login(deviceFunction.getDeviceId(), driverInfo);
        log.debug("设备[{}]登录成功，userId={}",deviceFunction.getDeviceId(), userId);
        Integer number = attribute(pointInfo, "number");
        boolean success = control(userId, number, value("int", deviceFunction.getValue().getValue()));
        if (!success){
            log.warn("设备[{}]，控制失败，value={}", deviceFunction.getDeviceId(), deviceFunction.getValue().getValue());
            throw new ServiceException("设备[" + deviceFunction.getDeviceId() + "]，控制失败，value=" + deviceFunction.getValue().getValue());
        }
        return true;
    }



}
