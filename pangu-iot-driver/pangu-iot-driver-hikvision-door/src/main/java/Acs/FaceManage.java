package Acs;

import NetSDKDemo.HCNetSDK;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  人脸管理模块
 * 实现功能:人脸下发(单张、多张)、人脸查询、人脸删除、人脸采集
 */
public class FaceManage {
    static int dwState = -1; //下发卡数据状态

    /**
     * 下发一张人脸
     *
     * @param lUserID 用户登录句柄
     * @param CardNo 卡号
     * @throws InterruptedException
     */
    public static void SetOneFace(int lUserID, String CardNo) throws InterruptedException {
        HCNetSDK.NET_DVR_FACE_COND struFaceCond = new HCNetSDK.NET_DVR_FACE_COND();
        struFaceCond.read();
        struFaceCond.dwSize = struFaceCond.size();
        struFaceCond.byCardNo = CardNo.getBytes();
        struFaceCond.dwFaceNum = 1;  //下发一张
        struFaceCond.dwEnableReaderNo = 1;//人脸读卡器编号
        struFaceCond.write();
        Pointer ptrStruFaceCond = struFaceCond.getPointer();
        int m_lSetFaceCfgHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_FACE, ptrStruFaceCond, struFaceCond.size(), null, null);
        if (m_lSetFaceCfgHandle == -1) {
            System.out.println("建立下发人脸长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立下发人脸长连接成功！");
        }
        HCNetSDK.NET_DVR_FACE_RECORD struFaceRecord = new HCNetSDK.NET_DVR_FACE_RECORD();
        struFaceRecord.read();
        struFaceRecord.dwSize = struFaceRecord.size();
        for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
            struFaceRecord.byCardNo[i] = 0;
        }
        for (int i = 0; i < CardNo.length(); i++) {
            struFaceRecord.byCardNo[i] = CardNo.getBytes()[i];
        }
        /*****************************************
         * 从本地文件里面读取JPEG图片二进制数据
         *****************************************/
        FileInputStream picfile = null;
        int picdataLength = 0;
        try {
            picfile = new FileInputStream(new File(System.getProperty("user.dir") + "\\pic\\FDLib.jpg"));
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
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        ptrpicByte.write();
        struFaceRecord.dwFaceLen = picdataLength;
        struFaceRecord.pFaceBuffer = ptrpicByte.getPointer();
        struFaceRecord.write();
        HCNetSDK.NET_DVR_FACE_STATUS struFaceStatus = new HCNetSDK.NET_DVR_FACE_STATUS();
        struFaceStatus.read();
        struFaceStatus.dwSize = struFaceStatus.size();
        struFaceStatus.write();
        IntByReference pInt = new IntByReference(0);
        while (true) {
            int dwFaceState = AcsMain.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(m_lSetFaceCfgHandle, struFaceRecord.getPointer(), struFaceRecord.size(), struFaceStatus.getPointer(), struFaceStatus.size(), pInt);
            struFaceStatus.read();
            if (dwFaceState == -1) {
                System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwFaceState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT) {
                System.out.println("配置等待");
                Thread.sleep(10);
                continue;
            } else if (dwFaceState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("下发人脸失败, 卡号: " + new String(struFaceStatus.byCardNo).trim() + ", 错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwFaceState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("下发人脸异常, 卡号: " + new String(struFaceStatus.byCardNo).trim() + ", 错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwFaceState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                if (struFaceStatus.byRecvStatus != 1) {
                    System.out.println("下发人脸失败，人脸读卡器状态：" + struFaceStatus.byRecvStatus + ", 下发错误信息：" + new String(struFaceStatus.byErrorMsg) + ", 卡号：" + new String(struFaceStatus.byCardNo).trim());
                    break;
                } else {
                    System.out.println("下发人脸成功, 卡号: " + new String(struFaceStatus.byCardNo).trim() + ", 状态：" + struFaceStatus.byRecvStatus);
                }
                continue;
            } else if (dwFaceState == HCNetSDK.NET_SDK_CONFIG_STATUS_FINISH) {
                System.out.println("下发人脸完成");
                break;
            }
        }
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lSetFaceCfgHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功");
        }
    }

    //批量下发多张人脸
    public static void SetMultiFace(int lUserID, String[] CardNo, String[] strFilePath, int iNum) throws InterruptedException {
        HCNetSDK.NET_DVR_FACE_COND struFaceCond = new HCNetSDK.NET_DVR_FACE_COND();
        struFaceCond.read();
        struFaceCond.dwSize = struFaceCond.size();
        struFaceCond.byCardNo = new byte[32]; //批量下发，该卡号不需要赋值
        struFaceCond.dwFaceNum = iNum;    //下发人脸个数
        struFaceCond.dwEnableReaderNo = 1;//人脸读卡器编号
        struFaceCond.write();
        Pointer ptrStruFaceCond = struFaceCond.getPointer();
        int m_lSetMultiFaceCfgHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_SET_FACE, ptrStruFaceCond, struFaceCond.size(), null, null);
        if (m_lSetMultiFaceCfgHandle == -1) {
            System.out.println("建立下发人脸长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立下发人脸长连接成功！");
        }
        HCNetSDK.NET_DVR_FACE_STATUS struFaceStatus = new HCNetSDK.NET_DVR_FACE_STATUS();
        struFaceStatus.read();
        struFaceStatus.dwSize = struFaceStatus.size();
        struFaceStatus.write();
        IntByReference pInt = new IntByReference(0);
        for (int i = 0; i < iNum; i++) {
            HCNetSDK.NET_DVR_FACE_RECORD struFaceRecord = new HCNetSDK.NET_DVR_FACE_RECORD();
            struFaceRecord.read();
            struFaceRecord.dwSize = struFaceRecord.size();
            for (int j = 0; j < HCNetSDK.ACS_CARD_NO_LEN; j++) {
                struFaceRecord.byCardNo[j] = 0;
            }
            for (int j = 0; j < CardNo[i].length(); j++) {
                struFaceRecord.byCardNo[j] = CardNo[i].getBytes()[j];
            }
            /*****************************************
             * 从本地文件里面读取JPEG图片二进制数据
             *****************************************/
            FileInputStream picfile = null;
            int picdataLength = 0;
            try {
                picfile = new FileInputStream(new File(strFilePath[i]));
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
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            ptrpicByte.write();
            struFaceRecord.dwFaceLen = picdataLength;
            struFaceRecord.pFaceBuffer = ptrpicByte.getPointer();
            struFaceRecord.write();
            int dwFaceState = AcsMain.hCNetSDK.NET_DVR_SendWithRecvRemoteConfig(m_lSetMultiFaceCfgHandle, struFaceRecord.getPointer(), struFaceRecord.size(), struFaceStatus.getPointer(), struFaceStatus.size(), pInt);
            struFaceStatus.read();
            if (dwFaceState == -1) {
                System.out.println("NET_DVR_SendWithRecvRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            } else if (dwFaceState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("下发人脸失败, 卡号: " + new String(struFaceStatus.byCardNo).trim() + ", 错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                //可以继续下发下一个
            } else if (dwFaceState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("下发人脸异常, 卡号: " + new String(struFaceStatus.byCardNo).trim() + ", 错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                //异常是长连接异常，不能继续下发后面的数据，需要重新建立长连接
                break;
            } else if (dwFaceState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                if (struFaceStatus.byRecvStatus != 1) {
                    System.out.println("下发人脸失败，人脸读卡器状态" + struFaceStatus.byRecvStatus + ", 卡号：" + new String(struFaceStatus.byCardNo).trim());
                } else {
                    System.out.println("下发人脸成功, 卡号: " + new String(struFaceStatus.byCardNo).trim() + ", 状态：" + struFaceStatus.byRecvStatus);
                }
                //可以继续下发下一个
            } else {
                System.out.println("其他状态：" + dwState);
            }
        }
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lSetMultiFaceCfgHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功\n");
        }
    }

    /**
     * 查询单张卡号关联的人脸信息
     *
     * @param lUserID 用户登录句柄
     * @param CardNo 卡号
     */
    public static void GetFaceCfg(int lUserID, String CardNo) {
        HCNetSDK.NET_DVR_FACE_COND struFaceCond = new HCNetSDK.NET_DVR_FACE_COND();
        struFaceCond.read();
        struFaceCond.dwSize = struFaceCond.size();
        struFaceCond.dwFaceNum = 1; //查询一个人脸参数
        struFaceCond.dwEnableReaderNo = 1;//读卡器编号
        for (int j = 0; j < HCNetSDK.ACS_CARD_NO_LEN; j++) {
            struFaceCond.byCardNo[j] = 0;
        }
        System.arraycopy(CardNo.getBytes(), 0, struFaceCond.byCardNo, 0, CardNo.getBytes().length);
        struFaceCond.write();
        int m_lGetFaceHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_GET_FACE, struFaceCond.getPointer(), struFaceCond.size(), null, null);
        if (m_lGetFaceHandle == -1) {
            System.out.println("建立查询人脸参数长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立查询人脸参数长连接成功！");
        }
        //查询结果
        HCNetSDK.NET_DVR_FACE_RECORD struFaceRecord = new HCNetSDK.NET_DVR_FACE_RECORD();
        struFaceRecord.read();
        while (true) {
            dwState = AcsMain.hCNetSDK.NET_DVR_GetNextRemoteConfig(m_lGetFaceHandle, struFaceRecord.getPointer(), struFaceRecord.size());
            struFaceRecord.read();
            if (dwState == -1) {
                System.out.println("NET_DVR_GetNextRemoteConfig查询人脸调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
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
                System.out.println("获取人脸参数失败, 卡号: " + CardNo);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                System.out.println("获取人脸参数异常, 卡号: " + CardNo);
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                if ((struFaceRecord.dwFaceLen > 0) && (struFaceRecord.pFaceBuffer != null)) {
                    FileOutputStream fout;
                    try {
                        String filename = System.getProperty("user.dir") + "\\pic\\" + CardNo + "_Face.jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struFaceRecord.pFaceBuffer.getByteBuffer(offset, struFaceRecord.dwFaceLen);
                        byte[] bytes = new byte[struFaceRecord.dwFaceLen];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                        System.out.println("获取人脸参数成功, 卡号: " + CardNo + "图片保存路径: " + filename);
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
                System.out.println("获取卡参数完成");
                break;
            }
        }
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(m_lGetFaceHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功");
        }
    }

    /**
     * 删除单独人脸信息
     *
     * @param lUserID 用户登录句柄
     * @param CardNo 卡号
     * @throws UnsupportedEncodingException
     * @throws InterruptedException
     */
    public static void DelOneFace(int lUserID, String CardNo) throws UnsupportedEncodingException, InterruptedException {
        HCNetSDK.NET_DVR_FACE_PARAM_CTRL struFaceDelCond = new HCNetSDK.NET_DVR_FACE_PARAM_CTRL();
        struFaceDelCond.dwSize = struFaceDelCond.size();
        struFaceDelCond.byMode = 0; //删除方式：0- 按卡号方式删除，1- 按读卡器删除
        struFaceDelCond.struProcessMode.setType(HCNetSDK.NET_DVR_FACE_PARAM_BYCARD.class);
        //需要删除人脸关联的卡号
        for (int i = 0; i < HCNetSDK.ACS_CARD_NO_LEN; i++) {
            struFaceDelCond.struProcessMode.struByCard.byCardNo[i] = 0;
        }
        System.arraycopy(CardNo.getBytes(), 0, struFaceDelCond.struProcessMode.struByCard.byCardNo, 0, CardNo.length());
        struFaceDelCond.struProcessMode.struByCard.byEnableCardReader[0] = 1; //读卡器
        struFaceDelCond.struProcessMode.struByCard.byFaceID[0] = 1; //人脸ID
        struFaceDelCond.write();
        Pointer ptrFaceDelCond = struFaceDelCond.getPointer();
        boolean bRet = AcsMain.hCNetSDK.NET_DVR_RemoteControl(lUserID, HCNetSDK.NET_DVR_DEL_FACE_PARAM_CFG, ptrFaceDelCond, struFaceDelCond.size());
        if (!bRet) {
            System.out.println("删除人脸失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("删除人脸成功！");
        }
    }

    /**
     * 人脸采集（设备采集人脸图片保存到本地）
     *
     * @param lUserID 用户登录句柄
     */
    public static void CaptureFaceInfo(int lUserID) {
        HCNetSDK.NET_DVR_CAPTURE_FACE_COND struCapCond = new HCNetSDK.NET_DVR_CAPTURE_FACE_COND();
        struCapCond.read();
        struCapCond.dwSize = struCapCond.size();
        struCapCond.write();
        int lCaptureFaceHandle = AcsMain.hCNetSDK.NET_DVR_StartRemoteConfig(lUserID, HCNetSDK.NET_DVR_CAPTURE_FACE_INFO, struCapCond.getPointer(), struCapCond.size(), null, null);
        if (lCaptureFaceHandle == -1) {
            System.out.println("建立采集人脸长连接失败，错误码为" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
            return;
        } else {
            System.out.println("建立采集人脸长连接成功！");
        }
        //采集的人脸信息
        HCNetSDK.NET_DVR_CAPTURE_FACE_CFG struFaceInfo = new HCNetSDK.NET_DVR_CAPTURE_FACE_CFG();
        struFaceInfo.read();
        while (true) {
            int dwState = AcsMain.hCNetSDK.NET_DVR_GetNextRemoteConfig(lCaptureFaceHandle, struFaceInfo.getPointer(), struFaceInfo.size());
            struFaceInfo.read();
            if (dwState == -1) {
                System.out.println("NET_DVR_GetNextRemoteConfig采集人脸失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_NEED_WAIT) {
                System.out.println("正在采集中,请等待...");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                continue;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_FAILED) {
                System.out.println("采集人脸失败");
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_EXCEPTION) {
                //超时时间5秒内设备本地人脸采集失败就会返回失败,连接会断开
                System.out.println("采集人脸异常, 网络异常导致连接断开 ");
                break;
            } else if (dwState == HCNetSDK.NET_SDK_CONFIG_STATUS_SUCCESS) {
                if ((struFaceInfo.dwFacePicSize > 0) && (struFaceInfo.pFacePicBuffer != null)) {
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String newName = sf.format(new Date());
                    FileOutputStream fout;
                    try {
                        String filename = System.getProperty("user.dir") + "\\pic\\" + newName + "_capFaceInfo.jpg";
                        fout = new FileOutputStream(filename);
                        //将字节写入文件
                        long offset = 0;
                        ByteBuffer buffers = struFaceInfo.pFacePicBuffer.getByteBuffer(offset, struFaceInfo.dwFacePicSize);
                        byte[] bytes = new byte[struFaceInfo.dwFacePicSize];
                        buffers.rewind();
                        buffers.get(bytes);
                        fout.write(bytes);
                        fout.close();
                        System.out.println("采集人脸成功, 图片保存路径: " + filename);
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
                System.out.println("其他异常, dwState: " + dwState);
                break;
            }
        }
        //采集成功之后断开连接、释放资源
        if (!AcsMain.hCNetSDK.NET_DVR_StopRemoteConfig(lCaptureFaceHandle)) {
            System.out.println("NET_DVR_StopRemoteConfig接口调用失败，错误码：" + AcsMain.hCNetSDK.NET_DVR_GetLastError());
        } else {
            System.out.println("NET_DVR_StopRemoteConfig接口成功");
        }
    }
}
