package Acs;
import NetSDKDemo.HCNetSDK;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import java.io.UnsupportedEncodingException;
/**
 * @create 2021-03-11-14:12
 */
public class DoorManage {

    /**
     * 门状态计划模板配置
     *
     * @param lUserID 用户登录句柄
     * @param iDoorTemplateNo 计划模板编号，为0表示取消关联，恢复默认状态（普通状态）
     */
    public static void DoorTemplate(int lUserID, int iDoorTemplateNo) {
        //设置门状态计划参数
        HCNetSDK.NET_DVR_DOOR_STATUS_PLAN struDoorStatus = new HCNetSDK.NET_DVR_DOOR_STATUS_PLAN();
        struDoorStatus.read();
        struDoorStatus.dwSize = struDoorStatus.size();
        struDoorStatus.dwTemplateNo = iDoorTemplateNo;
        struDoorStatus.write();
        boolean b_SetDoorStatus = AcsMain.hCNetSDK.NET_DVR_SetDVRConfig(lUserID, HCNetSDK.NET_DVR_SET_DOOR_STATUS_PLAN, 1, struDoorStatus.getPointer(), struDoorStatus.size());
        if (b_SetDoorStatus == false) {
            System.out.println("设置门状态计划参数，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("设置门状态计划参数成功");
        }
        //设置门状态计划模板参数
        HCNetSDK.NET_DVR_PLAN_TEMPLATE struDoorTemp = new HCNetSDK.NET_DVR_PLAN_TEMPLATE();
        struDoorTemp.read();
        struDoorTemp.dwSize = struDoorTemp.size();
        struDoorTemp.byEnable = 1;   //使能
        struDoorTemp.dwWeekPlanNo = 1;  //周计划模板编号；
        byte[] byTemplateName;
        try {
            byTemplateName = "DoorTemplatePlan_1".getBytes("GBK");
            //计划模板名称
            for (int i = 0; i < HCNetSDK.NAME_LEN; i++) {
                struDoorTemp.byTemplateName[i] = 0;
            }
            System.arraycopy(byTemplateName, 0, struDoorTemp.byTemplateName, 0, byTemplateName.length);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        struDoorTemp.write();
        boolean b_SetDoorTemp = AcsMain.hCNetSDK.NET_DVR_SetDVRConfig(lUserID, HCNetSDK.NET_DVR_SET_DOOR_STATUS_PLAN_TEMPLATE, iDoorTemplateNo, struDoorTemp.getPointer(), struDoorTemp.size());
        if (b_SetDoorTemp == false) {
            System.out.println("设置门状态计划模板失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("设置门状态计划模板成功");
        }
        //获取(设置)门状态周计划参数
        HCNetSDK.NET_DVR_WEEK_PLAN_CFG struDoorWeekPlan = new HCNetSDK.NET_DVR_WEEK_PLAN_CFG();
        struDoorWeekPlan.read();
        Pointer pstruDoorWeekPlan = struDoorWeekPlan.getPointer();
        IntByReference Ipint = new IntByReference(0);
        boolean b_GetPlan = AcsMain.hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_WEEK_PLAN_CFG, 1, pstruDoorWeekPlan, struDoorWeekPlan.size(), Ipint);
        if (b_GetPlan == false) {
            System.out.println("获取门状态周计划参数失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("获取门状态周计划参数成功");
        }
        struDoorWeekPlan.read();
        struDoorWeekPlan.byEnable = 1; //是否使能：0- 否，1- 是

        /**避免时间段交叉，先初始化*/
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[j].byEnable = 0;
                struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struBeginTime.byHour = 0;
                struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struBeginTime.byMinute = 0;
                struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struBeginTime.bySecond = 0;
                struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struEndTime.byHour = 0;
                struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struEndTime.byMinute = 0;
                struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struEndTime.bySecond = 0;
            }
        }

        /**一周7天，全天24小时*/
        for (int i = 0; i < 7; i++) {
            struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[0].byEnable = 1;
            struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.byHour = 0;
            struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.byMinute = 0;
            struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.bySecond = 0;
            struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.byHour = 23;
            struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.byMinute = 0;
            struDoorWeekPlan.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.bySecond = 0;
        }

        /**一周7天，每天设置2个时间段*/
        /*for(int i=0;i<7;i++)
        {
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].byEnable = 1;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.byHour = 0;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.byMinute = 0;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.bySecond = 0;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.byHour = 11;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.byMinute = 59;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.bySecond = 59;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[1].byEnable = 1;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[1].struTimeSegment.struBeginTime.byHour = 13;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[1].struTimeSegment.struBeginTime.byMinute = 30;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[1].struTimeSegment.struBeginTime.bySecond = 0;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[1].struTimeSegment.struEndTime.byHour = 19;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[1].struTimeSegment.struEndTime.byMinute = 59;
	            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[1].struTimeSegment.struEndTime.bySecond = 59;
	    }*/
        struDoorWeekPlan.write();
        boolean b_SetPlan = AcsMain.hCNetSDK.NET_DVR_SetDVRConfig(lUserID, HCNetSDK.NET_DVR_SET_WEEK_PLAN_CFG, 1, pstruDoorWeekPlan, struDoorWeekPlan.size());
        if (b_SetPlan == false) {
            System.out.println("获取设置状态周计划参数失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("设置门状态周计划参数成功");
        }
    }

    /**
     * 远程控门
     *
     * @param lUserID 用户登录句柄
     * @param lGatewayIndex 门禁序号（楼层编号、锁ID），从1开始，-1表示对所有门（或者梯控的所有楼层）进行操作
     * @param dwStaic 命令值：0- 关闭（对于梯控，表示受控），1- 打开（对于梯控，表示开门），2- 常开（对于梯控，表示自由、通道状态），3- 常关（对于梯控，表示禁用），4- 恢复（梯控，普通状态），5- 访客呼梯（梯控），6- 住户呼梯（梯控）
     */
    public static void ControlGateway(int lUserID, int lGatewayIndex, int dwStaic) {
        boolean b_Gate = AcsMain.hCNetSDK.NET_DVR_ControlGateway(lUserID, lGatewayIndex, dwStaic);
        if (b_Gate == false) {
            System.out.println("NET_DVR_ControlGateway远程控门失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        }else {
            System.out.println("远程控门成功");
        }
    }
}
