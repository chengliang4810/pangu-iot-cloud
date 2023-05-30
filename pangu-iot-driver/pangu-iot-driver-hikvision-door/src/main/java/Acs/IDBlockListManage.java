package Acs;

import NetSDKDemo.HCNetSDK;
import com.sun.jna.Pointer;

/**
 *身份证禁止名单下发，需要设备支持此功能，例如DS-5604
 * 实现功能：禁止人员名单下发、逐条删除、清空
 */


public class IDBlockListManage {
    public static FRemoteConfigCallback callback = null;

   public static class FRemoteConfigCallback implements HCNetSDK.FRemoteConfigCallBack {
        public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData)
        {
            System.err.println("NET_DVR_StartRemoteConfig Callback：" + dwType);
            switch (dwType)
            {
                case 0:
                    HCNetSDK.REMOTECONFIGSTATUS struCfgStatus  = new HCNetSDK.REMOTECONFIGSTATUS();
                    struCfgStatus.write();
                    Pointer pCfgStatus = struCfgStatus.getPointer();
                    pCfgStatus.write(0, lpBuffer.getByteArray(0, struCfgStatus.size()), 0,struCfgStatus.size());
                    struCfgStatus.read();
                    int iStatus = 0;
                    for(int i=0;i<4;i++)
                    {
                        int ioffset = i*8;
                        int iByte = struCfgStatus.byStatus[i]&0xff;
                        iStatus = iStatus + (iByte << ioffset);
                    }
                    switch (iStatus){
                        case 1000:// NET_SDK_CALLBACK_STATUS_SUCCESS
                            System.out.println("下发成功,dwStatus:" + iStatus);
                            break;
                        case 1001:
                            System.out.println("正在下发中,dwStatus:" + iStatus);
                            break;
                        case 1002:
                            int iErrorCode = 0;
                            for(int i=0;i<4;i++)
                            {
                                int ioffset = i*8;
                                int iByte = struCfgStatus.byErrorCode[i]&0xff;
                                iErrorCode = iErrorCode + (iByte << ioffset);
                            }
                            System.err.println("下发失败, dwStatus:" + iStatus + "错误号:" + iErrorCode);
                            break;
                    }
                    break;
                default:
                    break;
            }
        }

    }

    //逐条下发身份证禁止人员名单
    public static void UploadBlockList(int lUserID) {

        HCNetSDK.NET_DVR_UPLOAD_ID_BLOCKLIST_COND startParams = new HCNetSDK.NET_DVR_UPLOAD_ID_BLOCKLIST_COND();
        startParams.read();
        startParams.dwSize = startParams.size();
        startParams.dwBlockListNum = 1;     //下发禁止名单数量
        startParams.write();

        if (callback==null)
        {
            FRemoteConfigCallback callback=new FRemoteConfigCallback();
        }
        int lHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_BULK_UPLOAD_ID_BLOCKLIST, startParams.getPointer(), startParams.size(),callback, Pointer.NULL);

        if (lHandle < 0) {
            System.out.println("NET_DVR_StartRemoteConfig失败，错误号：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            HCNetSDK.NET_DVR_UPLOAD_ID_BLOCKLIST_CFG sendParams = new HCNetSDK.NET_DVR_UPLOAD_ID_BLOCKLIST_CFG();
            sendParams.read();
            sendParams.dwSize = sendParams.size();

            sendParams.struIDCardCfg.dwSize = sendParams.struIDCardCfg.size();
            sendParams.byBlockListValid = 1;   //身份证禁止名单是否有效：0-无效，1-有效（用于按身份证号码删除身份证禁止名单，该字段为0时代表删除）

            sendParams.struIDCardCfg.byName = "zhansn".getBytes();

            sendParams.struIDCardCfg.struBirth.read();
            sendParams.struIDCardCfg.struBirth.wYear = 1994;
            sendParams.struIDCardCfg.struBirth.byMonth = 03;
            sendParams.struIDCardCfg.struBirth.byDay = 02;

            sendParams.struIDCardCfg.byAddr = "浙江省杭州市".getBytes();
            sendParams.struIDCardCfg.byIDNum = "1111111111111111".getBytes();
            sendParams.struIDCardCfg.byIssuingAuthority = "浙江省杭州市".getBytes();


            sendParams.struIDCardCfg.struStartDate.wYear = 2020;
            sendParams.struIDCardCfg.struStartDate.byMonth = 4;
            sendParams.struIDCardCfg.struStartDate.byDay = 20;
            sendParams.struIDCardCfg.struEndDate.wYear = 2022;
            sendParams.struIDCardCfg.struEndDate.byMonth = 4;
            sendParams.struIDCardCfg.struEndDate.byDay = 20;

            sendParams.struIDCardCfg.byTermOfValidity = 0;
            sendParams.struIDCardCfg.bySex = 1;
            sendParams.struIDCardCfg.byNation = 1;
            sendParams.byBlockListValid = 1;
            sendParams.write();
            if (!AcsMain.hCNetSDK.NET_DVR_SendRemoteConfig(lHandle, 0x3, sendParams.getPointer(), sendParams.size())) {
                System.err.println("NET_DVR_SendRemoteConfig失败，错误号：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            } else {
                System.out.println("NET_DVR_SendRemoteConfig成功");
            }
        }

        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(lHandle)) {
            System.err.println("NET_DVR_StopRemoteConfig失败，错误号：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig成功");
        }

    }


    //清空禁止人员名单
    public static void CleanBlockList(int lUserID) {
        HCNetSDK.NET_DVR_ACS_PARAM_TYPE struAcsParam = new HCNetSDK.NET_DVR_ACS_PARAM_TYPE();
        struAcsParam.read();
        struAcsParam.dwSize = struAcsParam.size();
        struAcsParam.dwParamType = 0x00080000;   //身份证禁止名单参数
        struAcsParam.wLocalControllerID = 0;  // 就地控制器序号。0代表门禁设备
        struAcsParam.write();
        if (!AcsMain.hCNetSDK.NET_DVR_RemoteControl(lUserID, HCNetSDK.NET_DVR_CLEAR_ACS_PARAM, struAcsParam.getPointer(), struAcsParam.size())) {
            System.err.println("清空身份证禁止名单失败，错误码为"+AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("清除成功");
        }
    }
}
