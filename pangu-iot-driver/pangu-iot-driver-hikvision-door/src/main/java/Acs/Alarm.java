package Acs;

import NetSDKDemo.HCNetSDK;

import static Acs.AcsMain.fMSFCallBack_V31;
import static Acs.AcsMain.lAlarmHandle;

/**
 * @create 2021-03-12-13:53
 * <p>
 * 报警模块，实现功能：1、设备报警事件实时上传，报警事件包括刷脸、刷卡等认证事件和设备的操作事件
 * 2、事件主动获取（获取保存在设备上的事件）
 */
public final class Alarm {

    /**
     * 报警布防 （布防和监听选其一）
     *
     * @param lUserID 用户登录句柄
     */
    public static void SetAlarm(int lUserID) {

        //尚未布防,需要布防
        if (lAlarmHandle < 0) {
            //报警布防参数设置
            HCNetSDK.NET_DVR_SETUPALARM_PARAM m_strAlarmInfo = new HCNetSDK.NET_DVR_SETUPALARM_PARAM();
            m_strAlarmInfo.dwSize = m_strAlarmInfo.size();
            m_strAlarmInfo.byLevel = 1;  //布防等级
            m_strAlarmInfo.byAlarmInfoType = 1;   // 智能交通报警信息上传类型：0- 老报警信息（NET_DVR_PLATE_RESULT），1- 新报警信息(NET_ITS_PLATE_RESULT)
            m_strAlarmInfo.byDeployType = 0;   //布防类型 0：客户端布防 1：实时布防
            m_strAlarmInfo.write();
            lAlarmHandle = AcsMain.hCNetSDK.NET_DVR_SetupAlarmChan_V41(lUserID, m_strAlarmInfo);
            System.out.println("lAlarmHandle: " + lAlarmHandle);
            if (lAlarmHandle == -1) {
                System.out.println("布防失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            } else {
                System.out.println("布防成功");
            }
        }
    }

    /**
     * 报警监听
     *
     * 注意: 设置监听前需要现在本机pc配置端口
     * 具体步骤：库文件ClientDemo->登录设备->配置->网络参数配置->配置告警管理主机地址、告警管理主机端口（需要与NET_DVR_StartListen_V30接口中对应）
     */
    public static void StartListen() {

        //这里NET_DVR_StartListen_V30接口中的ip和端口需要和配置的ip和端口对应
        AcsMain.lListenHandle = AcsMain.hCNetSDK.NET_DVR_StartListen_V30("10.17.36.13", (short) 7201, fMSFCallBack_V31, null);
        if (AcsMain.lListenHandle == -1) {
            System.out.println("监听失败" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("监听成功");
        }
    }
}
