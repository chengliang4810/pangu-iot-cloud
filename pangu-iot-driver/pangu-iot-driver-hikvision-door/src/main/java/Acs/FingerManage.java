package Acs;

import NetSDKDemo.HCNetSDK;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 指纹管理模块，实现功能：指纹下发，指纹获取、指纹采集、指纹删除
 */
public class FingerManage {

    /**
     * 指纹下发
     *
     * @param lUserID 用户登录句柄
     * @param CardNo  卡号
     * @throws InterruptedException
     *
     * 注意: 下发的指纹数据长度为512位,如果直接调用采集接口从设备采集到的指纹数据长度为768位,需要做截取
     */
    public static void SetOneFinger(int lUserID, String CardNo) throws InterruptedException {
        HCNetSDK.NET_DVR_FINGERPRINT_COND struFingerCond = new HCNetSDK.NET_DVR_FINGERPRINT_COND();
        struFingerCond.read();
        struFingerCond.dwSize = struFingerCond.size();
        struFingerCond.dwFingerprintNum = 1;   //下发一个指纹
        for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
            struFingerCond.byCardNo[i] = 0;
        }
        for (int i = 0; i < CardNo.length(); i++) {
            struFingerCond.byCardNo[i] = CardNo.getBytes()[i];
        }
        struFingerCond.byFingerPrintID = 1;
        struFingerCond.dwEnableReaderNo = 1;
        struFingerCond.write();
        Pointer ptrStruCond = struFingerCond.getPointer();
        int m_lSetFingerCfgHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_FINGERPRINT, ptrStruCond, struFingerCond.size(), null, null);
        if (m_lSetFingerCfgHandle == -1) {
            System.out.println("建立下发指纹长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立下发指纹长连接成功！");
        }
        HCNetSDK.NET_DVR_FINGERPRINT_RECORD struFingerRecond = new HCNetSDK.NET_DVR_FINGERPRINT_RECORD();
        struFingerRecond.read();
        struFingerRecond.dwSize = struFingerRecond.size();
        for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
            struFingerRecond.byCardNo[i] = 0;
        }
        for (int i = 0; i < CardNo.length(); i++) {
            struFingerRecond.byCardNo[i] = CardNo.getBytes()[i];
        }
        struFingerRecond.byFingerPrintID = 1;
        struFingerRecond.dwEnableReaderNo = 1;
        struFingerRecond.byFingerType = 0;
        /*****************************************
         * 从本地文件里面读取指纹二进制数据
         *****************************************/
        FileInputStream picfile = null;
        int picdataLength = 0;
        try {
            picfile = new FileInputStream(new File(System.getProperty("user.dir") + "\\FingerData\\FingerTest.data"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            picdataLength = picfile.available();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (picdataLength < 0) {
            System.out.println("input file dataSize < 0");
            return;
        }

        HCNetSDK.BYTE_ARRAY ptrpicByte = new HCNetSDK.BYTE_ARRAY(picdataLength);
        try {
            picfile.read(ptrpicByte.byValue);
            picfile.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        ptrpicByte.write();

        /** 从采集到的指纹数据中截取前512位数据传入 */
        HCNetSDK.BYTE_ARRAY ptrpicByte_512 = new HCNetSDK.BYTE_ARRAY(512);
        ptrpicByte_512.read();
        System.arraycopy(ptrpicByte.byValue, 0, ptrpicByte_512.byValue, 0, 512);
        ptrpicByte_512.write();

        struFingerRecond.dwFingerPrintLen = ptrpicByte_512.byValue.length;
        struFingerRecond.byFingerData = ptrpicByte_512.byValue;
        struFingerRecond.write();
        Pointer pFingerRecond = struFingerRecond.getPointer();
        HCNetSDK.NET_DVR_FINGERPRINT_STATUS struFingerStatus = new HCNetSDK.NET_DVR_FINGERPRINT_STATUS();
        struFingerStatus.read();
        struFingerStatus.dwSize = struFingerStatus.size();
        struFingerStatus.write();
        IntByReference pInt = new IntByReference(0);
        Pointer pFingerStatus = struFingerStatus.getPointer();
        while (true) {
            int dwFingerState = AcsMain.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(m_lSetFingerCfgHandle, pFingerRecond, struFingerRecond.size(), pFingerStatus, struFingerStatus.size(), pInt);
            struFingerStatus.read();
            if (dwFingerState == -1) {
                System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwFingerState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT) {
                System.out.println("配置等待");
                Thread.sleep(10);
                continue;
            } else if (dwFingerState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("下发指纹失败, 卡号: " + new String(struFingerStatus.byCardNo).trim() + ", 错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwFingerState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("下发指纹异常, 卡号: " + new String(struFingerStatus.byCardNo).trim() + ", 错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwFingerState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                if (struFingerStatus.byRecvStatus != 0) {
                    System.out.println("下发指纹失败，读卡器状态: " + struFingerStatus.byCardReaderRecvStatus + ", 下发错误信息：" + new String(struFingerStatus.byErrorMsg) + ", 卡号：" + new String(struFingerStatus.byCardNo).trim());
                    break;
                } else {
                    System.out.println("下发指纹成功, 卡号: " + new String(struFingerStatus.byCardNo).trim() + ", 状态：" + struFingerStatus.byRecvStatus);
                }
                continue;
            } else if (dwFingerState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
                System.out.println("下发指纹完成");
                break;
            }
        }
    }

    /**
     * 指纹采集
     *
     * @param lUserID 用户登录句柄
     */
    public static void CaptureFinger(int lUserID) {
        HCNetSDK.NET_DVR_CAPTURE_FINGERPRINT_COND strFingerCond = new HCNetSDK.NET_DVR_CAPTURE_FINGERPRINT_COND();
        strFingerCond.read();
        strFingerCond.dwSize = strFingerCond.size();
        strFingerCond.byFingerPrintPicType = 1;
        strFingerCond.byFingerNo = 1;
        strFingerCond.write();
        int lGetFingerHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_CAPTURE_FINGERPRINT_INFO, strFingerCond.getPointer(), strFingerCond.dwSize, null, null);
        if (lGetFingerHandle == -1) {
            System.out.println("建立采集指纹长连接失败，错误码为：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立采集指纹长连接成功！");
        }
        HCNetSDK.NET_DVR_CAPTURE_FINGERPRINT_CFG strFingerCfg = new HCNetSDK.NET_DVR_CAPTURE_FINGERPRINT_CFG();
        strFingerCfg.read();
        while (true) {
            int dwFingerState = AcsMain.hCNetSDK.NET_DVR_GetNextRemoteConfig(lGetFingerHandle, strFingerCfg.getPointer(), strFingerCfg.size());
            strFingerCfg.read();
            if (dwFingerState == -1) {
                System.out.println("NET_DVR_GetNextRemoteConfig采集指纹失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwFingerState == HCNetSDK.NET_SDK_GET_NEXT_STATUS_FAILED) {
                System.out.println("采集指纹失败");
                break;
            } else if (dwFingerState == HCNetSDK.NET_SDK_GET_NEXT_STATUS_NEED_WAIT) {
                System.out.println("正在采集指纹中,请等待...");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            } else if (dwFingerState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                //超时时间5秒内设备本地人脸采集失败就会返回失败,连接会断开
                System.out.println("采集指纹异常, 网络异常导致连接断开 ");
                break;
            } else if (dwFingerState == HCNetSDK.NET_SDK_GET_NEXT_STATUS_SUCCESS) {
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                String newName = sf.format(new Date());
                String fileName = newName + "_capFinger.data";
                String filePath = System.getProperty("user.dir") + "\\FingerData\\" + fileName;
                BufferedOutputStream bos = null;
                FileOutputStream fos = null;
                File file = null;
                try {
                    File dir = new File(filePath);
                    if (!dir.exists() && dir.isDirectory()) {//判断文件目录是否存在
                        dir.mkdirs();
                    }
                    file = new File(filePath);
                    fos = new FileOutputStream(file);
                    bos = new BufferedOutputStream(fos);
                    bos.write(strFingerCfg.byFingerData);
                    System.out.println("采集指纹成功！");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null) {
                        try {
                            bos.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                if ((strFingerCfg.dwFingerPrintPicSize > 0) && (strFingerCfg.pFingerPrintPicBuffer != null)) {
                    FileOutputStream fout;
                    try {
                        String filename = System.getProperty("user.dir") + "\\FingerData\\" + newName + "_FingerPrintPic.jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = strFingerCfg.pFingerPrintPicBuffer.getByteBuffer(offset, strFingerCfg.dwFingerPrintPicSize);
                        byte[] bytes = new byte[strFingerCfg.dwFingerPrintPicSize];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                        System.out.println("采集指纹成功, 图片保存路径: " + filename);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            } else {
                System.out.println("其他异常, dwState: " + dwFingerState);
                break;
            }
        }
        //采集成功之后断开连接、释放资源
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(lGetFingerHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功");
        }
    }

    //指纹获取
    public static void GetOneFinger(int lUserID, String CardNo) {
        HCNetSDK.NET_DVR_FINGERPRINT_COND struFingerCond = new HCNetSDK.NET_DVR_FINGERPRINT_COND();
        struFingerCond.read();
        struFingerCond.dwSize = struFingerCond.size();
        struFingerCond.dwFingerprintNum = 1;   //下发一个指纹
        struFingerCond.dwEnableReaderNo = 1;
        struFingerCond.byFingerPrintID = 1;
        for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
            struFingerCond.byCardNo[i] = 0;
        }
        for (int i = 0; i < CardNo.length(); i++) {
            struFingerCond.byCardNo[i] = CardNo.getBytes()[i];
        }
        struFingerCond.write();
        Pointer ptrStruCond = struFingerCond.getPointer();
        int m_lGetFingerCfgHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_GET_FINGERPRINT, ptrStruCond, struFingerCond.size(), null, null);
        if (m_lGetFingerCfgHandle == -1) {
            System.out.println("建立获取指纹长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立获取指纹长连接成功！");
        }
        //查询结果
        HCNetSDK.NET_DVR_FINGERPRINT_RECORD struFingerRecord = new HCNetSDK.NET_DVR_FINGERPRINT_RECORD();
        struFingerRecord.read();
        while (true) {
            int dwState = AcsMain.hCNetSDK.NET_DVR_GetNextRemoteConfig(m_lGetFingerCfgHandle, struFingerRecord.getPointer(), struFingerRecord.size());
            struFingerRecord.read();
            if (dwState == -1) {
                System.out.println("NET_DVR_GetNextRemoteConfig查询指纹调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT) {
                System.out.println("查询中，请等待...");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("获取指纹参数失败, 卡号: " + CardNo);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("获取指纹参数异常, 卡号: " + CardNo);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                if ((struFingerRecord.dwFingerPrintLen > 0) && (struFingerRecord.byFingerData != null)) {
                    FileOutputStream fout;
                    try {
                        String filename = System.getProperty("user.dir") + "\\FingerData\\" + CardNo + "_Finger.data";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        InputStream is = new ByteArrayInputStream(struFingerRecord.byFingerData);
                        byte[] buff = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buff)) != -1) {
                            fout.write(buff, 0, len);
                        }
                        is.close();
                        fout.close();
                        System.out.println("获取指纹参数成功, 卡号: " + CardNo + "指纹保存路径: " + filename);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
                System.out.println("获取指纹参数完成");
                break;
            }
        }
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lGetFingerCfgHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功");
        }
    }


    //指纹删除
    public static void delFinger(int lUserID, String cardNo) {
        int iErr = 0;
        HCNetSDK.NET_DVR_FINGER_PRINT_INFO_CTRL_V50 m_struFingerDelInfoParam = new HCNetSDK.NET_DVR_FINGER_PRINT_INFO_CTRL_V50();
        m_struFingerDelInfoParam.dwSize = m_struFingerDelInfoParam.size();
        m_struFingerDelInfoParam.byMode = 0;// 删除方式，0-按卡号（人员ID）方式删除，1-按读卡器删除
        m_struFingerDelInfoParam.struProcessMode.setType(HCNetSDK.NET_DVR_FINGER_PRINT_BYCARD_V50.class);
        for (int i = 0; i < cardNo.length(); i++) {
            m_struFingerDelInfoParam.struProcessMode.struByCard.byCardNo[i] = cardNo.getBytes()[i];
        }
        m_struFingerDelInfoParam.struProcessMode.struByCard.byEnableCardReader[0] = 1;//指纹的读卡器信息，按位表示
        m_struFingerDelInfoParam.struProcessMode.struByCard.byFingerPrintID[0] = 1;//需要删除的指纹编号，按数组下标，值表示0-不删除，1-删除该指纹 ,指纹编号1删除


        Pointer lpInBuffer1 = m_struFingerDelInfoParam.getPointer();
        m_struFingerDelInfoParam.write();


        int lHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_DEL_FINGERPRINT, lpInBuffer1, m_struFingerDelInfoParam.size(), null, null);
        if (lHandle < 0) {
            iErr = AcsMain.hCNetSDK.NET_DVR_GetLastError();
            System.out.println("NET_DVR_DEL_FINGERPRINT_CFG_V50 建立长连接失败，错误号：" + iErr);
            return;
        }
        while (true) {
            HCNetSDK.NET_DVR_FINGER_PRINT_INFO_CTRL_V50 v50 = new HCNetSDK.NET_DVR_FINGER_PRINT_INFO_CTRL_V50();
            v50.dwSize = v50.size();
            v50.write();
            int res = AcsMain.hCNetSDK.NET_DVR_GetNextRemoteConfig(lHandle, v50.getPointer(), v50.size());
            if (res == 1002) {
                AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(lHandle);
                System.out.println("删除指纹成功！！！");
                break;
            } else if (res == 1003) {
                System.out.println("接口失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(lHandle);
                break;
            }
        }
    }
}
