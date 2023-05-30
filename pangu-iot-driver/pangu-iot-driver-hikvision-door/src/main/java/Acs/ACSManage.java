package Acs;

import NetSDKDemo.HCNetSDK;
import com.sun.jna.ptr.IntByReference;

/**
 * 门禁设备管理：
 * 1、获取（设置）门禁主机参数
 * 2、获取门禁主机工作状态
 */
public class ACSManage {

    /**
     * 获取（设置）门禁主机参数
     *
     * @param lUserID 用户登录句柄
     */
    public static void AcsCfg(int lUserID) {

        /**获取门禁主机参数*/
        HCNetSDK.NET_DVR_ACS_CFG struAcsCfg = new HCNetSDK.NET_DVR_ACS_CFG();
        struAcsCfg.dwSize = struAcsCfg.size();
        struAcsCfg.write();
        IntByReference intByReference = new IntByReference(0);
        boolean b_GetAcsCfg = AcsMain.hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_ACS_CFG, 0xFFFFFFFF, struAcsCfg.getPointer(),
                struAcsCfg.size(), intByReference);
        if (b_GetAcsCfg = false) {
            System.out.println("获取门禁主机参数，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("获取门禁主机参数成功");
            struAcsCfg.read();
            System.out.println("1.是否显示抓拍图片：" + struAcsCfg.byShowCapPic + "\n");  //是否显示抓拍图片， 0-不显示，1-显示
            System.out.println("2.是否显示卡号：" + struAcsCfg.byShowCardNo + "\n");   //是否显示卡号，0-不显示，1-显示
            System.out.println("3.是否开启语音提示：" + struAcsCfg.byVoicePrompt + "\n");  //是否启用语音提示，0-不启用，1-启用
            System.out.println("4.联动抓图是否上传：" + struAcsCfg.byUploadCapPic + "\n"); //联动抓拍是否上传图片，0-不上传，1-上传
        }

        /**设置门禁主机参数*/
        struAcsCfg.byShowCardNo = 1;     //开启显示卡号
        struAcsCfg.byVoicePrompt = 0;    //关闭语音提示
        struAcsCfg.byUploadCapPic = 1;   //开启联动抓图后，设备上抓拍的图片才会通过报警布防上传，否则没有不上传
        struAcsCfg.byShowCapPic = 1;
        struAcsCfg.write();
        boolean b_SetAcsCfg = AcsMain.hCNetSDK.NET_DVR_SetDVRConfig(lUserID, HCNetSDK.NET_DVR_SET_ACS_CFG, 0xFFFFFFFF, struAcsCfg.getPointer(), struAcsCfg.size());
        if (b_SetAcsCfg = false) {
            System.out.println("设置门禁主机参数，错误码为：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("设置门禁主机参数成功！！！");
        }
    }

    /**
     * 获取门禁主机工作状态
     *
     * @param lUserID 用户登录句柄
     */
    public static void getAcsStatus(int lUserID){

        HCNetSDK.NET_DVR_ACS_WORK_STATUS_V50 netDvrAcsWorkStatusV50 = new HCNetSDK.NET_DVR_ACS_WORK_STATUS_V50();
        netDvrAcsWorkStatusV50.dwSize = netDvrAcsWorkStatusV50.size();
        netDvrAcsWorkStatusV50.write();

        IntByReference intByReference = new IntByReference(0);
        boolean b_GetAcsStatus = AcsMain.hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_ACS_WORK_STATUS_V50, 0xFFFFFFFF, netDvrAcsWorkStatusV50.getPointer(),
                netDvrAcsWorkStatusV50.size(), intByReference);
        if (!b_GetAcsStatus) {
            System.out.println("获取门禁主机工作状态，错误码为：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("获取门禁主机工作状态成功！！！");
            netDvrAcsWorkStatusV50.read();
            System.out.println("1.门锁状态（或者梯控的继电器开合状态）：" + netDvrAcsWorkStatusV50.byDoorLockStatus[0] + "\n"); //门锁状态：0- 正常关，1- 正常开，2- 短路报警，3- 断路报警，4- 异常报警
            System.out.println("2.门状态（或者梯控的楼层状态）：" + netDvrAcsWorkStatusV50.byDoorStatus[0] + "\n"); //门状态（或者梯控的楼层状态）：1- 休眠，2- 常开状态（对于梯控，表示自由状态），3- 常闭状态（对于梯控，表示禁用状态），4- 普通状态（对于梯控，表示受控状态）
            System.out.println("3.门磁状态：" + netDvrAcsWorkStatusV50.byMagneticStatus[0] + "\n"); //门磁状态，0-正常关，1-正常开，2-短路报警，3-断路报警，4-异常报警
            System.out.println("4.事件报警输入状态：" + netDvrAcsWorkStatusV50.byCaseStatus[0] + "\n"); //事件报警输入状态：0- 无输入，1- 有输入
        }
    }


    /**
     * 远程控门
     * @param userID
     */
    public static void RemoteControlGate(int userID)
    {
        /**第二个参数lGatewayIndex
         [in] 门禁序号（楼层编号、锁ID），从1开始，-1表示对所有门（或者梯控的所有楼层）进行操作
         第三个参数dwStaic
         [in] 命令值：0- 关闭（对于梯控，表示受控），1- 打开（对于梯控，表示开门），2- 常开（对于梯控，表示自由、通道状态），3- 常关（对于梯控，表示禁用），4- 恢复（梯控，普通状态），5- 访客呼梯（梯控），6- 住户呼梯（梯控）*/
        boolean b_gate=AcsMain.hCNetSDK.NET_DVR_ControlGateway(userID,1,1);
        if (b_gate==false)
        {
            System.out.println("远程控门失败,err="+AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        System.out.println("远程控门成功");
        return;
    }
}
