package Acs;

import Commom.osSelect;
import NetSDKDemo.FMSGCallBack_V31;
import NetSDKDemo.HCNetSDK;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.io.UnsupportedEncodingException;

/**
 * 明眸门禁以卡为中心demo示例
 */
public class AcsMain {
    static HCNetSDK hCNetSDK = null;
    static int lUserID = -1;//用户句柄
    public static int lAlarmHandle = -1; //布防句柄
    public static int lListenHandle = -1; //监听句柄
    static int iCharEncodeType = 0;  //设备字符集
    public static FMSGCallBack_V31 fMSFCallBack_V31 = null;

    /**
     * @param args
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws UnsupportedEncodingException, InterruptedException {
        if (hCNetSDK == null) {
            if (!CreateSDKInstance()) {
                System.out.println("Load SDK fail");
                return;
            }
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

        /**加载日志*/
        boolean i = hCNetSDK.NET_DVR_SetLogToFile(3, "..//sdklog", false);
        //设置报警回调函数
        if (fMSFCallBack_V31 == null) {
            fMSFCallBack_V31 = new FMSGCallBack_V31();
            Pointer pUser = null;
            if (!AcsMain.hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(fMSFCallBack_V31, pUser)) {
                System.out.println("设置回调函数失败!");
            } else {
                System.out.println("设置回调函数成功!");
            }
        }


        /**登录*/
        Login("172.21.131.10", "admin", "abcd1234", (short) 8000);


        /**远程控门*/
        // DoorManage.ControlGateway(lUserID, 1, 0);

        /**启动报警布防*/
        Alarm.SetAlarm(lUserID);

        /**门禁主机参数设置（获取）*/
//        ACSManage.AcsCfg(lUserID);

        /**获取门禁主机工作状态*/
        ACSManage.getAcsStatus(lUserID);

        /**启动报警监听*/
//        Alarm.StartListen();

        /**设置卡计划模块*/
//        CardManage.SetCardTemplate(lUserID, 2);

        /**卡下发*/
//        CardManage.SetOneCard(lUserID, "123555", 2222, (short) 1);

        /**获取（查询）一张卡信息*/
//        CardManage.GetOneCard(lUserID, "123");

        /**获取所有卡*/
//        CardManage.GetAllCard(lUserID);

        /**批量卡号下发*/
/*        String[] CardNo = new String[]{"1111", "2222", "3333", "4444"};
        int[] EmployeeNo = new int[]{111, 222, 333, 444};
        CardManage.SetMultiCard(lUserID, CardNo, EmployeeNo, 4);*/

        /**删除单张卡（删除单张卡号之前要先删除这张卡关联的人脸和指纹信息）*/
//        CardManage.DelOneCard(lUserID,"1111");

        /**清空设备所有人脸、指纹、卡号信息*/
//        CardManage.CleanCardInfo(lUserID);

        /**下发一张人脸*/
        /**注意：下发人脸之前先下发卡号 */
//        FaceManage.SetOneFace(lUserID,"4444");

        /**删除单独人脸信息*/
//        FaceManage.DelOneFace(lUserID,"4444");

        /**查询单张卡号关联的人脸信息*/
//        FaceManage.GetFaceCfg(lUserID,"4444");

        /**人脸采集（设备采集人脸图片保存到本地）*/
//        FaceManage.CaptureFaceInfo(lUserID);

        /**门状态计划模板配置*/
//        DoorManage.DoorTemplate(lUserID,1);

        /**指纹采集*/
//        FingerManage.CaptureFinger(lUserID);

        /**指纹下发*/
//        FingerManage.SetOneFinger(lUserID,"123555");

        /**指纹删除*/

//        FingerManage.delFinger(lUserID,"123123");


        /**查询设备事件*/
//        eventSearch.SearchAllEvent(lUserID);

        /**下发身份证禁止名单*/
//         IDBlockListManage.UploadBlockList(lUserID);

        /**清空身份证禁止名单*/
//        IDBlockListManage.CleanBlockList(lUserID);

        /**
         * 增加sleep时间，保证程序一直运行，

         */
        Thread.sleep(30000);

        /**登出操作*/
        AcsMain.Logout();
    }

    /**
     * 设备登录
     *
     * @param ipadress IP地址
     * @param user     用户名
     * @param psw      密码
     * @param port     端口，默认8000
     */
    public static void Login(String ipadress, String user, String psw, short port) {
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
        lUserID = hCNetSDK.NET_DVR_Login_V40(m_strLoginInfo, m_strDeviceInfo);
        if (lUserID == -1) {
            System.out.println("登录失败，错误码为" + hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("登录成功！");
            m_strDeviceInfo.read();
            iCharEncodeType = m_strDeviceInfo.byCharEncodeType;
            return;
        }
    }

    /**
     * 登出操作
     */
    public static void Logout() {

        /**退出之前判断布防监听状态，并做撤防和停止监听操作*/
        if (lAlarmHandle >= 0) {
            if (!hCNetSDK.NET_DVR_CloseAlarmChan_V30(lAlarmHandle)) {
                System.out.println("撤防失败，错误码：" + hCNetSDK.NET_DVR_GetLastError());
            } else {
                System.out.println("撤防成功！！！");
            }
        }
        if (lListenHandle >= 0) {
            if (!hCNetSDK.NET_DVR_StopListen_V30(lListenHandle)) {
                System.out.println("取消监听失败，错误码：" + hCNetSDK.NET_DVR_GetLastError());
            } else {
                System.out.println("停止监听成功！！！");
            }
        }

        /**登出和清理，释放SDK资源*/
        if (lUserID >= 0) {
            hCNetSDK.NET_DVR_Logout(lUserID);
        }
        hCNetSDK.NET_DVR_Cleanup();
    }


    /**
     * 根据不同操作系统选择不同的库文件和库路径
     *
     * @return
     */
    private static boolean CreateSDKInstance() {
        if (hCNetSDK == null) {
            synchronized (HCNetSDK.class) {
                String strDllPath = "";
                try {
                    //System.setProperty("jna.debug_load", "true");
                    if (osSelect.isWindows())
                        //win系统加载库路径
                        strDllPath = System.getProperty("user.dir") + "\\lib\\HCNetSDK.dll";
                    else if (osSelect.isLinux())
                        //Linux系统加载库路径
                        strDllPath = System.getProperty("user.dir") + "/lib/libhcnetsdk.so";
                    hCNetSDK = (HCNetSDK) Native.loadLibrary(strDllPath, HCNetSDK.class);
                } catch (Exception ex) {
                    System.out.println("loadLibrary: " + strDllPath + " Error: " + ex.getMessage());
                    return false;
                }
            }
        }
        return true;
    }
}
