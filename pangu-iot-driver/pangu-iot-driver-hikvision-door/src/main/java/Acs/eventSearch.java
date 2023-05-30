package Acs;

import NetSDKDemo.HCNetSDK;
import com.sun.jna.Pointer;

import java.io.UnsupportedEncodingException;

/**
 * 事件查询模块
 */
public final class eventSearch {

    public static void SearchAllEvent(int lUserID) throws UnsupportedEncodingException, InterruptedException {
        int i = 0; //事件条数
        HCNetSDK.NET_DVR_ACS_EVENT_COND struAcsEventCond = new HCNetSDK.NET_DVR_ACS_EVENT_COND();
        struAcsEventCond.read();
        struAcsEventCond.dwSize = struAcsEventCond.size();
        //查询全部主次类型的报警
        struAcsEventCond.dwMajor = 0; // 主次事件类型设为0，代表查询所有事件
        struAcsEventCond.dwMinor = 0; //
        //开始时间
        struAcsEventCond.struStartTime.dwYear = 2021;
        struAcsEventCond.struStartTime.dwMonth = 12;
        struAcsEventCond.struStartTime.dwDay = 16;
        struAcsEventCond.struStartTime.dwHour = 9;
        struAcsEventCond.struStartTime.dwMinute = 0;
        struAcsEventCond.struStartTime.dwSecond = 0;
        //结束时间
        struAcsEventCond.struEndTime.dwYear = 2021;
        struAcsEventCond.struEndTime.dwMonth = 12;
        struAcsEventCond.struEndTime.dwDay = 16;
        struAcsEventCond.struEndTime.dwHour = 15;
        struAcsEventCond.struEndTime.dwMinute = 0;
        struAcsEventCond.struEndTime.dwSecond = 0;
        struAcsEventCond.wInductiveEventType = 1;
        struAcsEventCond.write();
        Pointer ptrStruEventCond = struAcsEventCond.getPointer();
        int m_lSearchEventHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_GET_ACS_EVENT, ptrStruEventCond, struAcsEventCond.size(), null, null);
        if (m_lSearchEventHandle<=-1)
        {
            System.out.println("NET_DVR_StartRemoteConfig调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        }
        HCNetSDK.NET_DVR_ACS_EVENT_CFG struAcsEventCfg = new HCNetSDK.NET_DVR_ACS_EVENT_CFG();
        struAcsEventCfg.read();
        struAcsEventCfg.dwSize = struAcsEventCfg.size();
        struAcsEventCfg.write();
        Pointer ptrStruEventCfg = struAcsEventCfg.getPointer();
        while (true) {
            System.out.println("i=" + i);
            int dwEventSearch = AcsMain.hCNetSDK.NET_DVR_GetNextRemoteConfig(m_lSearchEventHandle, ptrStruEventCfg, struAcsEventCfg.size());
            if (dwEventSearch <= -1) {
                System.out.println("NET_DVR_GetNextRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            }
            if (dwEventSearch == HCNetSDK.NET_SDK_GET_NEXT_STATUS_NEED_WAIT) {
                System.out.println("配置等待....");
                Thread.sleep(10);
                continue;
            } else if (dwEventSearch == HCNetSDK.NET_SDK_NEXT_STATUS__FINISH) {
                System.out.println("获取事件完成");
                break;
            } else if (dwEventSearch == HCNetSDK.NET_SDK_GET_NEXT_STATUS_FAILED) {
                System.out.println("获取事件出现异常");
                break;
            } else if (dwEventSearch == HCNetSDK.NET_SDK_GET_NEXT_STATUS_SUCCESS) {
                struAcsEventCfg.read();
                //获取的事件信息通过struAcsEventCfg结构体进行解析，
                System.out.println(i + "获取事件成功, 报警主类型：" + Integer.toHexString(struAcsEventCfg.dwMajor) + "报警次类型：" + Integer.toHexString(struAcsEventCfg.dwMinor) + "卡号：" + new String(struAcsEventCfg.struAcsEventInfo.byCardNo).trim());
                /**
                 * 工号有两个字段，dwEmployeeNo表示工号，当值为0时，表示无效，解析byEmployeeNo参数
                 */
                System.out.println("工号1："+struAcsEventCfg.struAcsEventInfo.dwEmployeeNo);
                System.out.println("工号2："+new String(struAcsEventCfg.struAcsEventInfo.byEmployeeNo));
                //人脸温度数据，需要设备和支持测温功能
                System.out.println("人脸温度：" + struAcsEventCfg.struAcsEventInfo.fCurrTemperature + "是否异常:" + struAcsEventCfg.struAcsEventInfo.byIsAbnomalTemperature);
                //口罩功能，需要设备支持此功能 0-保留，1-未知，2-不戴口罩，3-戴口罩
                System.out.println("是否带口罩:"+struAcsEventCfg.struAcsEventInfo.byMask);
                i++;
                continue;
            }
        }
        i = 0;
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lSearchEventHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功");
        }
        return;
    }

}
