package Acs;

import NetSDKDemo.HCNetSDK;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * 功能：人脸下发、查询、删除、人员计划模板配置
 */
public class UserManage {
    /**
     * 添加人员
     * @param lUserID 登录句柄
     * @param employeeNo  工号
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     * @throws JSONException
     */
    public void AddUserInfo(int lUserID,String employeeNo) throws UnsupportedEncodingException, InterruptedException, JSONException {
        HCNetSDK.BYTE_ARRAY ptrByteArray = new HCNetSDK.BYTE_ARRAY(1024);    //数组
        //"POST /ISAPI/AccessControl/UserInfo/Record?format=json" 此URL也是下发人员
        String strInBuffer = "PUT /ISAPI/AccessControl/UserInfo/SetUp?format=json";
        System.arraycopy(strInBuffer.getBytes(), 0, ptrByteArray.byValue, 0, strInBuffer.length());//字符串拷贝到数组中
        ptrByteArray.write();

        int lHandler = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_JSON_CONFIG, ptrByteArray.getPointer(), strInBuffer.length(),null, null);
        if (lHandler < 0)
        {
            System.out.println("AddUserInfo NET_DVR_StartRemoteConfig 失败,错误码为"+AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        else{
            System.out.println("AddUserInfo NET_DVR_StartRemoteConfig 成功!");

            byte[] Name = "测试".getBytes("utf-8"); //根据iCharEncodeType判断，如果iCharEncodeType返回6，则是UTF-8编码。
            //如果是0或者1或者2，则是GBK编码

            //将中文字符编码之后用数组拷贝的方式，避免因为编码导致的长度问题
            String strInBuffer1 = "{\"UserInfo\":{\"Valid\":{\"beginTime\":\"2017-08-01T17:30:08\",\"enable\":true,\"endTime\":" +
                    "\"2030-08-01T17:30:08\"}," +
                    "\"checkUser\":false,\"doorRight\":\"1\",\"RightPlan\":[{\"doorNo\": 1,\"planTemplateNo\": \"1,3,5\"}]," +
                    "\"employeeNo\":\""+employeeNo+"\",\"floorNumber\":2,\"maxOpenDoorTime\":0,\"name\":\"";
            String strInBuffer2 = "\",\"openDelayEnabled\":false,\"password\":\"123456\",\"roomNumber\":4,\"userType\":\"normal\"}}";
            int iStringSize = Name.length + strInBuffer1.length() + strInBuffer2.length();

            HCNetSDK.BYTE_ARRAY ptrByte = new HCNetSDK.BYTE_ARRAY(iStringSize);
            System.arraycopy(strInBuffer1.getBytes(), 0, ptrByte.byValue, 0, strInBuffer1.length());
            System.arraycopy(Name, 0, ptrByte.byValue, strInBuffer1.length(), Name.length);
            System.arraycopy(strInBuffer2.getBytes(), 0, ptrByte.byValue, strInBuffer1.length() + Name.length, strInBuffer2.length());
            ptrByte.write();

            System.out.println(new String(ptrByte.byValue));

            HCNetSDK.BYTE_ARRAY ptrOutuff = new HCNetSDK.BYTE_ARRAY(1024);

            IntByReference pInt = new IntByReference(0);
            while(true){
                int dwState = AcsMain.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(lHandler, ptrByte.getPointer(), iStringSize ,ptrOutuff.getPointer(), 1024,  pInt);
                //读取返回的json并解析
                ptrOutuff.read();
                String strResult = new String(ptrOutuff.byValue).trim();
                System.out.println("dwState:" + dwState + ",strResult:" + strResult);

                JSONObject jsonResult = new JSONObject(strResult);
                int statusCode = jsonResult.getInt("statusCode");
                String statusString = jsonResult.getString("statusString");


                if(dwState == -1){
                    System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                    break;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT)
                {
                    System.out.println("配置等待");
                    Thread.sleep(10);
                    continue;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED)
                {
                    System.out.println("下发人员失败, json retun:" + jsonResult.toString());
                    break;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION)
                {
                    System.out.println("下发人员异常, json retun:" + jsonResult.toString());
                    break;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS)
                {//返回NET_SDK_CONFIG_STATUS_SUCCESS代表流程走通了，但并不代表下发成功，比如有些设备可能因为人员已存在等原因下发失败，所以需要解析Json报文
                    if (statusCode != 1){
                        System.out.println("下发人员成功,但是有异常情况:" + jsonResult.toString());
                    }
                    else{
                        System.out.println("下发人员成功: json retun:" + jsonResult.toString());
                    }
                    break;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
                    //下发人员时：dwState其实不会走到这里，因为设备不知道我们会下发多少个人，所以长连接需要我们主动关闭
                    System.out.println("下发人员完成");
                    break;
                }
            }
            if(!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(lHandler)){
                System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            }
            else{
                System.out.println("NET_DVR_StopRemoteConfig接口成功");
            }
        }
    }


    public void SearchUserInfo(int userID) throws JSONException{
        HCNetSDK.BYTE_ARRAY ptrByteArray = new HCNetSDK.BYTE_ARRAY(1024);    //数组
        String strInBuffer = "POST /ISAPI/AccessControl/UserInfo/Search?format=json";
        System.arraycopy(strInBuffer.getBytes(), 0, ptrByteArray.byValue, 0, strInBuffer.length());//字符串拷贝到数组中
        ptrByteArray.write();

        int lHandler = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(userID, HCNetSDK.NET_DVR_JSON_CONFIG, ptrByteArray.getPointer(), strInBuffer.length(), null, null);
        if(lHandler < 0){
            System.out.println("SearchUserInfo NET_DVR_StartRemoteConfig 失败,错误码为"+AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        else{
            //组装查询的JSON报文，这边查询的是所有的人员
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonSearchCond = new JSONObject();

            //如果需要查询指定的工号人员信息，把下面注释的内容去除掉即可
	/*		JSONArray EmployeeNoList = new JSONArray();
			JSONObject employeeNo1 = new JSONObject();
			employeeNo1.put("employeeNo", "12346");
			JSONObject employeeNo2 = new JSONObject();
			employeeNo2.put("employeeNo", "1000");
			EmployeeNoList.put(employeeNo1);
			EmployeeNoList.put(employeeNo2);
			jsonSearchCond.put("EmployeeNoList", EmployeeNoList);*/

            jsonSearchCond.put("searchID", "20211126");
            jsonSearchCond.put("searchResultPosition", 0);
            jsonSearchCond.put("maxResults", 50);
            jsonObject.put("UserInfoSearchCond", jsonSearchCond);

            String strInbuff = jsonObject.toString();
            System.out.println("查询的json报文:" + strInbuff);

            //把string传递到Byte数组中，后续用.getPointer()方法传入指针地址中。
            HCNetSDK.BYTE_ARRAY ptrInbuff = new HCNetSDK.BYTE_ARRAY(strInbuff.length());
            System.arraycopy(strInbuff.getBytes(), 0, ptrInbuff.byValue, 0, strInbuff.length());
            ptrInbuff.write();

            //定义接收结果的结构体
            HCNetSDK.BYTE_ARRAY ptrOutuff = new HCNetSDK.BYTE_ARRAY(10*1024);

            IntByReference pInt = new IntByReference(0);

            while(true){
                int dwState = AcsMain.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(lHandler, ptrInbuff.getPointer(), strInbuff.length(), ptrOutuff.getPointer(), 10*1024, pInt);
                System.out.println(dwState);
                if(dwState == -1){
                    System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                    break;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT)
                {
                    System.out.println("配置等待");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    continue;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED)
                {
                    System.out.println("查询人员失败");
                    break;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION)
                {
                    System.out.println("查询人员异常");
                    break;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS)
                {
                    ptrOutuff.read();
                    System.out.println("查询人员成功, json:" + new String(ptrOutuff.byValue).trim());
                    break;
                }
                else if(dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
                    System.out.println("获取人员完成");
                    break;
                }
            }

            if(!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(lHandler)){
                System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            }
            else{
                System.out.println("NET_DVR_StopRemoteConfig接口成功");
                lHandler = -1;
            }
        }


    }

    public void deleteUserInfo(int userID)
    {
            //删除单个人员
//            String deleteUserjson="{\"UserInfoDelCond\": {\"EmployeeNoList\": [{\"employeeNo\": \"12346\"}]}}";
            //删除所有人员
            String deleteUserjson ="{\"UserInfoDelCond\": {\"EmployeeNoList\": []}}";
            String deleteUserUrl="PUT /ISAPI/AccessControl/UserInfo/Delete?format=json";
            String result=transIsapi.put_isapi(userID,deleteUserUrl,deleteUserjson);
            System.out.println(result);
            //获取删除进度
        HCNetSDK.BYTE_ARRAY ptrByteArray = new HCNetSDK.BYTE_ARRAY(1024);    //数组
        String strInBuffer = "GET /ISAPI/AccessControl/UserInfoDetail/DeleteProcess?format=json";
        System.arraycopy(strInBuffer.getBytes(), 0, ptrByteArray.byValue, 0, strInBuffer.length());//字符串拷贝到数组中
        ptrByteArray.write();
        int lHandler = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(userID, HCNetSDK.NET_DVR_JSON_CONFIG, ptrByteArray.getPointer(), strInBuffer.length(),null, null);
        if (lHandler < 0)
        {
            System.out.println("DeleteProcess NET_DVR_StartRemoteConfig 失败,错误码为"+AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        else{
            System.out.println("DeleteProcess NET_DVR_StartRemoteConfig 成功!");
    }
        while(true){
            HCNetSDK.BYTE_ARRAY ptrOutuff = new HCNetSDK.BYTE_ARRAY(1024);
            int dwState = AcsMain.hCNetSDK.NET_DVR_GetNextRemoteConfig(lHandler,ptrOutuff.getPointer(),ptrOutuff.byValue.length);
            if(dwState == -1){
                System.out.println("NET_DVR_GetNextRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            }
            else if(dwState == 1001)
            {
                System.out.println("配置等待");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            else if(dwState == 1002)
            {
                System.out.println("获取删除人员进度失败");
                AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(lHandler);
                break;
            }

            else if(dwState == 1000)
            {//返回NET_SDK_CONFIG_STATUS_SUCCESS代表流程走通了，但并不代表下发成功，比如有些设备可能因为人员已存在等原因下发失败，所以需要解析Json报文
                ptrOutuff.read();
                String strResult = new String(ptrOutuff.byValue).trim();
                System.out.println("strResult:"+strResult);
                continue;
            }
            else if(dwState == 1003) {
                AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(lHandler);
                System.out.println("获取删除人员进度完成");
                break;
            }
        }
    }

    /**
     * 人员计划模板配置
     *
     * @param userID 用户登录句柄
     * @param iPlanTemplateNumber 计划模板编号，从1开始，最大值从门禁能力集获取
     */
    public void SetCardTemplate(int userID, int iPlanTemplateNumber) {
        //设置卡权限计划模板参数
        HCNetSDK.NET_DVR_PLAN_TEMPLATE_COND struPlanCond = new HCNetSDK.NET_DVR_PLAN_TEMPLATE_COND();
        struPlanCond.dwSize = struPlanCond.size();
        struPlanCond.dwPlanTemplateNumber = iPlanTemplateNumber;//计划模板编号，从1开始，最大值从门禁能力集获取
        struPlanCond.wLocalControllerID = 0;//就地控制器序号[1,64]，0表示门禁主机
        struPlanCond.write();
        HCNetSDK.NET_DVR_PLAN_TEMPLATE struPlanTemCfg = new HCNetSDK.NET_DVR_PLAN_TEMPLATE();
        struPlanTemCfg.dwSize = struPlanTemCfg.size();
        struPlanTemCfg.byEnable = 1; //是否使能：0- 否，1- 是
        struPlanTemCfg.dwWeekPlanNo = 2;//周计划编号，0表示无效
        struPlanTemCfg.dwHolidayGroupNo[0] = 0;//假日组编号，按值表示，采用紧凑型排列，中间遇到0则后续无效
        byte[] byTemplateName;
        try {
            byTemplateName = "CardTemplatePlan_2".getBytes("GBK");
            //计划模板名称
            for (int i = 0; i < HCNetSDK.NAME_LEN; i++) {
                struPlanTemCfg.byTemplateName[i] = 0;
            }
            System.arraycopy(byTemplateName, 0, struPlanTemCfg.byTemplateName, 0, byTemplateName.length);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        struPlanTemCfg.write();
        IntByReference pInt = new IntByReference(0);
        Pointer lpStatusList = pInt.getPointer();
        if (false == AcsMain.hCNetSDK.NET_DVR_SetDeviceConfig(userID, HCNetSDK.NET_DVR_SET_CARD_RIGHT_PLAN_TEMPLATE_V50, 1, struPlanCond.getPointer(), struPlanCond.size(), lpStatusList, struPlanTemCfg.getPointer(), struPlanTemCfg.size())) {
            System.out.println("NET_DVR_SET_CARD_RIGHT_PLAN_TEMPLATE_V50失败，错误号：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        System.out.println("NET_DVR_SET_CARD_RIGHT_PLAN_TEMPLATE_V50成功！");
        //获取卡权限周计划参数
        HCNetSDK.NET_DVR_WEEK_PLAN_COND struWeekPlanCond = new HCNetSDK.NET_DVR_WEEK_PLAN_COND();
        struWeekPlanCond.dwSize = struWeekPlanCond.size();
        struWeekPlanCond.dwWeekPlanNumber = 2;
        struWeekPlanCond.wLocalControllerID = 0;
        HCNetSDK.NET_DVR_WEEK_PLAN_CFG struWeekPlanCfg = new HCNetSDK.NET_DVR_WEEK_PLAN_CFG();
        struWeekPlanCond.write();
        struWeekPlanCfg.write();
        Pointer lpCond = struWeekPlanCond.getPointer();
        Pointer lpInbuferCfg = struWeekPlanCfg.getPointer();
        if (false == AcsMain.hCNetSDK.NET_DVR_GetDeviceConfig(userID, HCNetSDK.NET_DVR_GET_CARD_RIGHT_WEEK_PLAN_V50, 1, lpCond, struWeekPlanCond.size(), lpStatusList, lpInbuferCfg, struWeekPlanCfg.size())) {
            System.out.println("NET_DVR_GET_CARD_RIGHT_WEEK_PLAN_V50失败，错误号：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        }
        struWeekPlanCfg.read();
        struWeekPlanCfg.byEnable = 1; //是否使能：0- 否，1- 是
        /**避免时间段交叉，先初始化， 七天八小时*/
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[j].byEnable = 0;
                struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struBeginTime.byHour = 0;
                struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struBeginTime.byMinute = 0;
                struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struBeginTime.bySecond = 0;
                struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struEndTime.byHour = 0;
                struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struEndTime.byMinute = 0;
                struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[j].struTimeSegment.struEndTime.bySecond = 0;
            }
        }
        /**一周7天，全天24小时*/
        for (int i = 0; i < 7; i++) {
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].byEnable = 1;
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.byHour = 21;
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.byMinute = 0;
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.bySecond = 0;
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.byHour = 23;
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.byMinute = 0;
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.bySecond = 0;
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
        struWeekPlanCfg.write();
        //设置卡权限周计划参数
        if (false == AcsMain.hCNetSDK.NET_DVR_SetDeviceConfig(userID, HCNetSDK.NET_DVR_SET_CARD_RIGHT_WEEK_PLAN_V50, 1, lpCond, struWeekPlanCond.size(), lpStatusList, lpInbuferCfg, struWeekPlanCfg.size())) {
            System.out.println("NET_DVR_SET_CARD_RIGHT_WEEK_PLAN_V50失败，错误号：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        }else {
            System.out.println("NET_DVR_SET_CARD_RIGHT_WEEK_PLAN_V50成功！");
        }
    }

}
