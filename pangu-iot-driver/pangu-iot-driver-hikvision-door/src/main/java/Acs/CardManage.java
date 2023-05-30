package Acs;

import NetSDKDemo.HCNetSDK;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.UnsupportedEncodingException;

/**

 * @create 2021-03-12-13:53
 * 以卡为中心，先下发卡参数（可以一起下发工号），再根据卡号下发人脸、指纹等参数
 * 卡管理模块，实现功能：卡下发、卡获取(单张、所有)、卡删除（单张、所有）、卡计划模块设置
 */
public class CardManage {
    public static short iPlanTemplateNumber;

    /**
     * 卡下发
     *
     * @param lUserID 用户登录句柄
     * @param CardNo 卡号
     * @param EmployeeNo 工号 卡下发的时候可以一起下发工号
     * @param iPlanTemplateNumber 关联门计划模板，计划模板的配置可以参考卡计划模板配置模块，(下发卡前要设置好计划模板)
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */
    public static void SetOneCard(int lUserID, String CardNo, int EmployeeNo, short iPlanTemplateNumber) throws UnsupportedEncodingException, InterruptedException {
        HCNetSDK.NET_DVR_CARD_COND struCardCond = new HCNetSDK.NET_DVR_CARD_COND();
        struCardCond.read();
        struCardCond.dwSize = struCardCond.size();
        struCardCond.dwCardNum = 1;  //下发一张
        struCardCond.write();
        Pointer ptrStruCond = struCardCond.getPointer();
        int m_lSetCardCfgHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_CARD, ptrStruCond, struCardCond.size(), null, null);
        if (m_lSetCardCfgHandle == -1) {
            System.out.println("建立下发卡长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立下发卡长连接成功！");
        }
        HCNetSDK.NET_DVR_CARD_RECORD struCardRecord = new HCNetSDK.NET_DVR_CARD_RECORD();
        struCardRecord.read();
        struCardRecord.dwSize = struCardRecord.size();
        for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
            struCardRecord.byCardNo[i] = 0;
        }
        for (int i = 0; i < CardNo.length(); i++) {
            struCardRecord.byCardNo[i] = CardNo.getBytes()[i];
        }
        struCardRecord.byCardType = 1; //普通卡
        struCardRecord.byLeaderCard = 0; //是否为首卡，0-否，1-是
        struCardRecord.byUserType = 0;
        struCardRecord.byDoorRight[0] = 1; //门1有权限

        struCardRecord.wCardRightPlan[0] = iPlanTemplateNumber;   //关联门计划模板，计划模板的配置可以参考卡计划模板配置模块，(下发卡前要设置好计划模板)
        struCardRecord.struValid.byEnable = 1;    //卡有效期使能，下面是卡有效期从2000-1-1 11:11:11到2030-1-1 11:11:11
        struCardRecord.struValid.struBeginTime.wYear = 2020;
        struCardRecord.struValid.struBeginTime.byMonth = 1;
        struCardRecord.struValid.struBeginTime.byDay = 1;
        struCardRecord.struValid.struBeginTime.byHour = 11;
        struCardRecord.struValid.struBeginTime.byMinute = 11;
        struCardRecord.struValid.struBeginTime.bySecond = 11;
        struCardRecord.struValid.struEndTime.wYear = 2022;
        struCardRecord.struValid.struEndTime.byMonth = 1;
        struCardRecord.struValid.struEndTime.byDay = 1;
        struCardRecord.struValid.struEndTime.byHour = 11;
        struCardRecord.struValid.struEndTime.byMinute = 11;
        struCardRecord.struValid.struEndTime.bySecond = 11;
        struCardRecord.dwEmployeeNo = EmployeeNo; //工号 卡下发的时候可以一起下发工号
        if ((AcsMain.iCharEncodeType == 0) || (AcsMain.iCharEncodeType == 1) || (AcsMain.iCharEncodeType == 2)) {
            byte[] strCardName = "jx".getBytes("GBK");  //姓名
            for (int i = 0; i < HCNetSDK.NAME_LEN; i++) {
                struCardRecord.byName[i] = 0;
            }
            System.arraycopy(strCardName, 0, struCardRecord.byName, 0, strCardName.length);
        }
        if (AcsMain.iCharEncodeType == 6) {
            byte[] strCardName = "jx".getBytes("UTF-8");  //姓名
            for (int i = 0; i < HCNetSDK.NAME_LEN; i++) {
                struCardRecord.byName[i] = 0;
            }
            System.arraycopy(strCardName, 0, struCardRecord.byName, 0, strCardName.length);
        }
        struCardRecord.write();
        HCNetSDK.NET_DVR_CARD_STATUS struCardStatus = new HCNetSDK.NET_DVR_CARD_STATUS();
        struCardStatus.read();
        struCardStatus.dwSize = struCardStatus.size();
        struCardStatus.write();
        IntByReference pInt = new IntByReference(0);
        while (true) {
            int dwState = AcsMain.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(m_lSetCardCfgHandle, struCardRecord.getPointer(), struCardRecord.size(), struCardStatus.getPointer(), struCardStatus.size(), pInt);
            struCardStatus.read();
            if (dwState == -1) {
                System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT) {
                System.out.println("配置等待");
                Thread.sleep(10);
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("下发卡失败, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 错误码：" + struCardStatus.dwErrorCode);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("下发卡异常, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 错误码：" + struCardStatus.dwErrorCode);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                if (struCardStatus.dwErrorCode != 0) {
                    System.out.println("下发卡成功,但是错误码" + struCardStatus.dwErrorCode + ", 卡号：" + new String(struCardStatus.byCardNo).trim());
                } else {
                    System.out.println("下发卡成功, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 状态：" + struCardStatus.byStatus);
                }
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
                System.out.println("下发卡完成");
                break;
            }
        }
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lSetCardCfgHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功\n");
        }
    }

    /**
     * 批量卡号下发
     *
     * @param lUserID 用户登录句柄
     * @param CardNo 卡号
     * @param iEmployeeNo 工号
     * @param iNum 下发张数
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */
    public static void SetMultiCard(int lUserID, String[] CardNo, int[] iEmployeeNo, int iNum) throws UnsupportedEncodingException, InterruptedException {
        HCNetSDK.NET_DVR_CARD_COND struCardCond = new HCNetSDK.NET_DVR_CARD_COND();
        struCardCond.read();
        struCardCond.dwSize = struCardCond.size();
        struCardCond.dwCardNum = iNum;  //下发张数
        struCardCond.write();
        Pointer ptrStruCond = struCardCond.getPointer();
        int m_lSetMultiCardCfgHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_CARD, ptrStruCond, struCardCond.size(), null, null);
        if (m_lSetMultiCardCfgHandle == -1) {
            System.out.println("建立下发卡长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立下发卡长连接成功！");
        }
        HCNetSDK.NET_DVR_CARD_RECORD[] struCardRecord = (HCNetSDK.NET_DVR_CARD_RECORD[]) new HCNetSDK.NET_DVR_CARD_RECORD().toArray(iNum);
        HCNetSDK.NET_DVR_CARD_STATUS struCardStatus = new HCNetSDK.NET_DVR_CARD_STATUS();
        struCardStatus.read();
        struCardStatus.dwSize = struCardStatus.size();
        struCardStatus.write();
        IntByReference pInt = new IntByReference(0);
        for (int i = 0; i < iNum; i++) {
            struCardRecord[i].read();
            struCardRecord[i].dwSize = struCardRecord[i].size();
            for (int j = 0; j < HCNetSDK.ACS_CARD_NO_LEN; j++) {
                struCardRecord[i].byCardNo[j] = 0;
            }
            System.arraycopy(CardNo[i].getBytes(), 0, struCardRecord[i].byCardNo, 0, CardNo[i].getBytes().length);
            struCardRecord[i].byCardType = 1; //普通卡
            struCardRecord[i].byLeaderCard = 0; //是否为首卡，0-否，1-是
            struCardRecord[i].byUserType = 0;
            struCardRecord[i].byDoorRight[0] = 1; //门1有权限
            struCardRecord[i].wCardRightPlan[0] = iPlanTemplateNumber;//关联门计划模板，使用了前面配置的计划模板
            struCardRecord[i].struValid.byEnable = 1;    //卡有效期使能，下面是卡有效期从2000-1-1 11:11:11到2030-1-1 11:11:11
            struCardRecord[i].struValid.struBeginTime.wYear = 2000;
            struCardRecord[i].struValid.struBeginTime.byMonth = 1;
            struCardRecord[i].struValid.struBeginTime.byDay = 1;
            struCardRecord[i].struValid.struBeginTime.byHour = 11;
            struCardRecord[i].struValid.struBeginTime.byMinute = 11;
            struCardRecord[i].struValid.struBeginTime.bySecond = 11;
            struCardRecord[i].struValid.struEndTime.wYear = 2030;
            struCardRecord[i].struValid.struEndTime.byMonth = 1;
            struCardRecord[i].struValid.struEndTime.byDay = 1;
            struCardRecord[i].struValid.struEndTime.byHour = 11;
            struCardRecord[i].struValid.struEndTime.byMinute = 11;
            struCardRecord[i].struValid.struEndTime.bySecond = 11;
            struCardRecord[i].dwEmployeeNo = iEmployeeNo[i]; //工号
            if ((AcsMain.iCharEncodeType == 0) || (AcsMain.iCharEncodeType == 1) || (AcsMain.iCharEncodeType == 2)) {
                byte[] strCardName = "张三".getBytes("GBK");  //姓名
                for (int j = 0; j < HCNetSDK.NAME_LEN; j++) {
                    struCardRecord[i].byName[j] = 0;
                }
                System.arraycopy(strCardName, 0, struCardRecord[i].byName, 0, strCardName.length);
            }
            if (AcsMain.iCharEncodeType == 6) {
                byte[] strCardName = "张三".getBytes("UTF-8");  //姓名
                for (int j = 0; j < HCNetSDK.NAME_LEN; j++) {
                    struCardRecord[i].byName[j] = 0;
                }
                System.arraycopy(strCardName, 0, struCardRecord[i].byName, 0, strCardName.length);
            }
            struCardRecord[i].write();
            int dwState = AcsMain.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(m_lSetMultiCardCfgHandle, struCardRecord[i].getPointer(), struCardRecord[i].size(), struCardStatus.getPointer(), struCardStatus.size(), pInt);
            struCardStatus.read();
            if (dwState == -1) {
                System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("下发卡失败, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 错误码：" + struCardStatus.dwErrorCode);
                //可以继续下发下一个
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("下发卡异常, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 错误码：" + struCardStatus.dwErrorCode);
                //异常是长连接异常，不能继续下发后面的数据，需要重新建立长连接
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                if (struCardStatus.dwErrorCode != 0) {
                    System.out.println("下发卡失败,错误码" + struCardStatus.dwErrorCode + ", 卡号：" + new String(struCardStatus.byCardNo).trim());
                } else {
                    System.out.println("下发卡成功, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 状态：" + struCardStatus.byStatus);
                }
                //可以继续下发下一个
            } else {
                System.out.println("其他状态：" + dwState);
            }
        }
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lSetMultiCardCfgHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功\n");
        }
    }

    /**
     * 获取（查询）一张卡
     *
     * @param lUserID 用户登录句柄
     * @param CardNo 卡号
     */
    public static void GetOneCard(int lUserID, String CardNo) {
        HCNetSDK.NET_DVR_CARD_COND struCardCond = new HCNetSDK.NET_DVR_CARD_COND();
        struCardCond.read();
        struCardCond.dwSize = struCardCond.size();
        struCardCond.dwCardNum = 1;  //查询一个卡参数
        struCardCond.write();
        Pointer ptrStruCond = struCardCond.getPointer();
        int m_lGetCardCfgHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_GET_CARD, ptrStruCond, struCardCond.size(), null, null);
        if (m_lGetCardCfgHandle == -1) {
            System.out.println("建立查询卡参数长连接失败，错误码为：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立查询卡参数长连接成功！");
        }
        //查找指定卡号的参数，需要下发查找的卡号条件
        HCNetSDK.NET_DVR_CARD_SEND_DATA struCardNo = new HCNetSDK.NET_DVR_CARD_SEND_DATA();
        struCardNo.read();
        struCardNo.dwSize = struCardNo.size();
        for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
            struCardNo.byCardNo[i] = 0;
        }
        for (int i = 0; i < CardNo.length(); i++) {
            struCardNo.byCardNo[i] = CardNo.getBytes()[i];
        }
        struCardNo.write();
        HCNetSDK.NET_DVR_CARD_RECORD struCardRecord = new HCNetSDK.NET_DVR_CARD_RECORD();
        struCardRecord.read();
        IntByReference pInt = new IntByReference(0);
        while (true) {
            int dwState = AcsMain.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(m_lGetCardCfgHandle, struCardNo.getPointer(), struCardNo.size(),
                    struCardRecord.getPointer(), struCardRecord.size(), pInt);
            struCardRecord.read();
            if (dwState == -1) {
                System.out.println("NET_DVR_SendWithRecvRemoteConfig查询卡参数调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT) {
                System.out.println("配置等待");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("获取卡参数失败, 卡号: " + CardNo);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("获取卡参数异常, 卡号: " + CardNo);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                try {
                    String strName = "";
                    if ((AcsMain.iCharEncodeType == 0) || (AcsMain.iCharEncodeType == 1) || (AcsMain.iCharEncodeType == 2)) {
                        strName = new String(struCardRecord.byName, "GBK").trim();
                    }
                    if (AcsMain.iCharEncodeType == 6) {
                        strName = new String(struCardRecord.byName, "UTF-8").trim();
                    }
                    //获取的卡信息打印
                    System.out.println("获取卡参数成功, 卡号: " + new String(struCardRecord.byCardNo).trim()
                            + ", 卡类型：" + struCardRecord.byCardType
                            + ", 姓名：" + strName);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
                System.out.println("获取卡参数完成");
                break;
            }
        }
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lGetCardCfgHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功\n");
        }
    }

    /**
     * 获取所有卡
     *
     * @param lUserID 用户登录句柄
     */
    public static void GetAllCard(int lUserID) {
        HCNetSDK.NET_DVR_CARD_COND struCardCond = new HCNetSDK.NET_DVR_CARD_COND();
        struCardCond.read();
        struCardCond.dwSize = struCardCond.size();
        struCardCond.dwCardNum = 0xffffffff; //查询所有卡
        struCardCond.write();
        Pointer ptrStruCond = struCardCond.getPointer();
        int m_lGetAllCardCfgHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_GET_CARD, ptrStruCond, struCardCond.size(), null, null);
        if (m_lGetAllCardCfgHandle == -1) {
            System.out.println("建立查询卡长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立查询卡长连接成功！");
        }
        HCNetSDK.NET_DVR_CARD_RECORD struCardRecord = new HCNetSDK.NET_DVR_CARD_RECORD();
        struCardRecord.read();
        struCardRecord.dwSize = struCardRecord.size();
        struCardRecord.write();
        IntByReference pInt = new IntByReference(0);
        while (true) {
            int dwState = AcsMain.hCNetSDK.NET_DVR_GetNextRemoteConfig(m_lGetAllCardCfgHandle, struCardRecord.getPointer(), struCardRecord.size());
            struCardRecord.read();
            if (dwState == -1) {
                System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT) {
                System.out.println("配置等待");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("获取卡参数失败");
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("获取卡参数异常");
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                try {
                    String strName = "";
                    if ((AcsMain.iCharEncodeType == 0) || (AcsMain.iCharEncodeType == 1) || (AcsMain.iCharEncodeType == 2)) {
                        strName = new String(struCardRecord.byName, "GBK").trim();
                    }
                    if (AcsMain.iCharEncodeType == 6) {
                        strName = new String(struCardRecord.byName, "UTF-8").trim();
                    }
                    //获取的卡信息打印
                    System.out.println("获取卡参数成功, 卡号: " + new String(struCardRecord.byCardNo).trim()
                            + ", 卡类型：" + struCardRecord.byCardType
                            + ", 姓名：" + strName);
                } catch (UnsupportedEncodingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
                System.out.println("获取卡参数完成");
                break;
            }
        }
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lGetAllCardCfgHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功\n");
        }
    }

    /**
     * 删除单张卡（删除单张卡号之前要先删除这张卡关联的人脸和指纹信息）
     *
     * @param lUserID 用户登录句柄
     * @param CardNo 卡号
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */
    public static void DelOneCard(int lUserID, String CardNo) throws UnsupportedEncodingException, InterruptedException {
        HCNetSDK.NET_DVR_CARD_COND struCardCond = new HCNetSDK.NET_DVR_CARD_COND();
        struCardCond.read();
        struCardCond.dwSize = struCardCond.size();
        struCardCond.dwCardNum = 1;  //删除一张卡号
        struCardCond.write();
        Pointer ptrStruCond = struCardCond.getPointer();
        int m_lDelCardCfgHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_DEL_CARD, ptrStruCond, struCardCond.size(), null, null);
        if (m_lDelCardCfgHandle == -1) {
            System.out.println("建立删除卡长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立删除卡长连接成功！");
        }
        HCNetSDK.NET_DVR_CARD_SEND_DATA struCardData = new HCNetSDK.NET_DVR_CARD_SEND_DATA();
        struCardData.read();
        struCardData.dwSize = struCardData.size();
        for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
            struCardData.byCardNo[i] = 0;
        }
        for (int i = 0; i < CardNo.length(); i++) {
            struCardData.byCardNo[i] = CardNo.getBytes()[i];
        }
        struCardData.write();
        HCNetSDK.NET_DVR_CARD_STATUS struCardStatus = new HCNetSDK.NET_DVR_CARD_STATUS();
        struCardStatus.read();
        struCardStatus.dwSize = struCardStatus.size();
        struCardStatus.write();
        IntByReference pInt = new IntByReference(0);
        while (true) {
            int dwState = AcsMain.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(m_lDelCardCfgHandle, struCardData.getPointer(), struCardData.size(), struCardStatus.getPointer(), struCardStatus.size(), pInt);
            struCardStatus.read();
            if (dwState == -1) {
                System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT) {
                System.out.println("配置等待");
                Thread.sleep(10);
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("删除卡失败, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 错误码：" + struCardStatus.dwErrorCode);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("删除卡异常, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 错误码：" + struCardStatus.dwErrorCode);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                if (struCardStatus.dwErrorCode != 0) {
                    System.out.println("删除卡成功,但是错误码" + struCardStatus.dwErrorCode + ", 卡号：" + new String(struCardStatus.byCardNo).trim());
                } else {
                    System.out.println("删除卡成功, 卡号: " + new String(struCardStatus.byCardNo).trim() + ", 状态：" + struCardStatus.byStatus);
                }
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
                System.out.println("删除卡完成");
                break;
            }
        }
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lDelCardCfgHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功\n");
        }
    }

    /**
     * 清空设备卡号、人脸、指纹信息
     * 清空设备所有人脸、指纹、卡号信息
     *
     * 设备中要清除所有已经下发的卡号，必须先和卡号关联的人脸信息、指纹信息，人脸和指纹清除后清楚，再调用清空卡号的接口，清空设备上所有卡号
     * @param lUserID 用户登录句柄
     */
    public static void CleanCardInfo(int lUserID) {
        //清空所有人脸信息
        HCNetSDK.NET_DVR_FACE_PARAM_CTRL struFaceCtrl = new HCNetSDK.NET_DVR_FACE_PARAM_CTRL();
        struFaceCtrl.read();
        struFaceCtrl.dwSize = struFaceCtrl.size();
        struFaceCtrl.byMode = 1;  //删除方式：0- 按卡号方式删除，1- 按读卡器删除
        if (struFaceCtrl.byMode == 1) {
            struFaceCtrl.struProcessMode.setType(HCNetSDK.NET_DVR_FACE_PARAM_BYREADER.class);
            struFaceCtrl.struProcessMode.struByReader.dwCardReaderNo = 1;   //读卡器编号
            struFaceCtrl.struProcessMode.struByReader.byClearAllCard = 1;   //是否删除所有卡的人脸信息：0- 按卡号删除人脸信息，1- 删除所有卡的人脸信息
        }
        struFaceCtrl.write();
        boolean b_face = AcsMain.hCNetSDK.NET_DVR_RemoteControl(lUserID, HCNetSDK.NET_DVR_DEL_FACE_PARAM_CFG, struFaceCtrl.getPointer(), struFaceCtrl.size());
        if (b_face == false) {
            System.out.println("删除人脸错误，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("删除人脸成功");
        }
        //清空所有指纹
        HCNetSDK.NET_DVR_FINGER_PRINT_INFO_CTRL struFingerCtrl = new HCNetSDK.NET_DVR_FINGER_PRINT_INFO_CTRL();
        struFingerCtrl.read();
        struFingerCtrl.dwSize = struFingerCtrl.size();
        struFingerCtrl.byMode = 1;  //删除方式：0- 按卡号方式删除，1- 按读卡器删除
        if (struFingerCtrl.byMode == 1) {
            struFingerCtrl.struProcessMode.setType(HCNetSDK.NET_DVR_FINGER_PRINT_BYREADER.class);
            struFingerCtrl.struProcessMode.struByReader.dwCardReaderNo = 1;   //读卡器编号
            struFingerCtrl.struProcessMode.struByReader.byClearAllCard = 1;   //是否删除所有卡的指纹信息：0- 按卡号删除指纹信息，1- 删除所有卡的指纹信息
        }
        struFingerCtrl.write();
        boolean b_finger = AcsMain.hCNetSDK.NET_DVR_RemoteControl(lUserID, HCNetSDK.NET_DVR_DEL_FINGERPRINT_CFG, struFingerCtrl.getPointer(), struFingerCtrl.size());
        if (b_finger == false) {
            System.out.println("删除指纹错误，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("删除指纹成功");
        }
        //清空所有卡号
        HCNetSDK.NET_DVR_ACS_PARAM_TYPE struAcsPapamType = new HCNetSDK.NET_DVR_ACS_PARAM_TYPE();
        struAcsPapamType.read();
        struAcsPapamType.dwSize = struAcsPapamType.size();
        struAcsPapamType.dwParamType = HCNetSDK.ACS_PARAM_CARD;  //清空卡参数
        struAcsPapamType.wLocalControllerID = 0;    //  就地控制器序号[1,255],0代表门禁主机
        struAcsPapamType.write();
        boolean b_AcsCard = AcsMain.hCNetSDK.NET_DVR_RemoteControl(lUserID, HCNetSDK.NET_DVR_CLEAR_ACS_PARAM, struAcsPapamType.getPointer(), struAcsPapamType.size());
        if (b_AcsCard == false) {
            System.out.println("清空卡号错误，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("清空卡号成功");
        }
    }

    /**
     * 卡计划模板配置
     *
     * @param lUserID 用户登录句柄
     * @param iPlanTemplateNumber 计划模板编号，从1开始，最大值从门禁能力集获取
     */
    public static void SetCardTemplate(int lUserID, int iPlanTemplateNumber) {
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
        if (false == AcsMain.hCNetSDK.NET_DVR_SetDeviceConfig(lUserID, HCNetSDK.NET_DVR_SET_CARD_RIGHT_PLAN_TEMPLATE_V50, 1, struPlanCond.getPointer(), struPlanCond.size(), lpStatusList, struPlanTemCfg.getPointer(), struPlanTemCfg.size())) {
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
        if (false == AcsMain.hCNetSDK.NET_DVR_GetDeviceConfig(lUserID, HCNetSDK.NET_DVR_GET_CARD_RIGHT_WEEK_PLAN_V50, 1, lpCond, struWeekPlanCond.size(), lpStatusList, lpInbuferCfg, struWeekPlanCfg.size())) {
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
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.byHour = 14;
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.byMinute = 0;
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struBeginTime.bySecond = 0;
            struWeekPlanCfg.struPlanCfg[i].struPlanCfgDay[0].struTimeSegment.struEndTime.byHour = 16;
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
        if (false == AcsMain.hCNetSDK.NET_DVR_SetDeviceConfig(lUserID, HCNetSDK.NET_DVR_SET_CARD_RIGHT_WEEK_PLAN_V50, 1, lpCond, struWeekPlanCond.size(), lpStatusList, lpInbuferCfg, struWeekPlanCfg.size())) {
            System.out.println("NET_DVR_SET_CARD_RIGHT_WEEK_PLAN_V50失败，错误号：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        }else {
            System.out.println("NET_DVR_SET_CARD_RIGHT_WEEK_PLAN_V50成功！");
        }
    }
}
