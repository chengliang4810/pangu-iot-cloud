package Acs;

import NetSDKDemo.HCNetSDK;

/**
 * @create 2021-04-13-15:23
 * 功能：透传接口实现，透传ISAPI命令
 */
public final class transIsapi {
    public static String get_isapi(int lUserID, String url) {
        HCNetSDK.NET_DVR_XML_CONFIG_INPUT struXMLInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
        struXMLInput.read();
        HCNetSDK.BYTE_ARRAY stringRequest = new HCNetSDK.BYTE_ARRAY(url.length());
        stringRequest.read();
        //输入ISAPI协议命令
        System.arraycopy(url.getBytes(), 0, stringRequest.byValue, 0, url.length());
        stringRequest.write();
        struXMLInput.dwSize = struXMLInput.size();
        struXMLInput.lpRequestUrl = stringRequest.getPointer();
        struXMLInput.dwRequestUrlLen = url.length();
        struXMLInput.lpInBuffer = null;
        struXMLInput.dwInBufferSize = 0;
        struXMLInput.write();

        HCNetSDK.BYTE_ARRAY stringXMLOut = new HCNetSDK.BYTE_ARRAY(8 * 1024);
        stringXMLOut.read();
        HCNetSDK.BYTE_ARRAY struXMLStatus = new HCNetSDK.BYTE_ARRAY(1024);
        struXMLStatus.read();
        HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struXMLOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
        struXMLOutput.read();
        struXMLOutput.dwSize = struXMLOutput.size();
        struXMLOutput.lpOutBuffer = stringXMLOut.getPointer();
        struXMLOutput.dwOutBufferSize = stringXMLOut.size();
        struXMLOutput.lpStatusBuffer = struXMLStatus.getPointer();
        struXMLOutput.dwStatusSize = struXMLStatus.size();
        struXMLOutput.write();
        if (!AcsMain.hCNetSDK.NET_DVR_STDXMLConfig(lUserID, struXMLInput, struXMLOutput)) {
            int iErr = AcsMain.hCNetSDK.NET_DVR_GetLastError();
            System.err.println("NET_DVR_STDXMLConfig失败，错误号" + iErr);
            return null;
        } else {
            stringXMLOut.read();
            System.out.println("输出文本大小：" + struXMLOutput.dwReturnedXMLSize);
            //打印输出XML文本
            String strOutXML = new String(stringXMLOut.byValue.toString()).trim();
            System.out.println(strOutXML);
            struXMLStatus.read();
            String strStatus = new String(struXMLStatus.byValue.toString()).trim();
            System.out.println(strStatus);
            return strOutXML;
        }
    }

    public static String put_isapi(int lUserID, String url, String inputXml) {
        HCNetSDK.NET_DVR_XML_CONFIG_INPUT struXMLInput = new HCNetSDK.NET_DVR_XML_CONFIG_INPUT();
        struXMLInput.read();
        HCNetSDK.BYTE_ARRAY stringRequest = new HCNetSDK.BYTE_ARRAY(url.length());
        stringRequest.read();
        //输入ISAPI协议命令
        System.arraycopy(url.getBytes(), 0, stringRequest.byValue, 0, url.length());
        stringRequest.write();
        struXMLInput.dwSize = struXMLInput.size();
        struXMLInput.lpRequestUrl = stringRequest.getPointer();
        struXMLInput.dwRequestUrlLen = url.length();
        HCNetSDK.BYTE_ARRAY ptrInBuffer = new HCNetSDK.BYTE_ARRAY(inputXml.length());
        ptrInBuffer.read();
        System.arraycopy(inputXml.getBytes(), 0, ptrInBuffer.byValue, 0, inputXml.length());
        ptrInBuffer.write();
        struXMLInput.lpInBuffer = ptrInBuffer.getPointer();
        struXMLInput.dwInBufferSize = inputXml.length();
        struXMLInput.write();
        HCNetSDK.BYTE_ARRAY stringXMLOut = new HCNetSDK.BYTE_ARRAY(8 * 1024);
        stringXMLOut.read();
        HCNetSDK.BYTE_ARRAY struXMLStatus = new HCNetSDK.BYTE_ARRAY(1024);
        struXMLStatus.read();
        HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT struXMLOutput = new HCNetSDK.NET_DVR_XML_CONFIG_OUTPUT();
        struXMLOutput.read();
        struXMLOutput.dwSize = struXMLOutput.size();
        struXMLOutput.lpOutBuffer = stringXMLOut.getPointer();
        struXMLOutput.dwOutBufferSize = stringXMLOut.size();
        struXMLOutput.lpStatusBuffer = struXMLStatus.getPointer();
        struXMLOutput.dwStatusSize = struXMLStatus.size();
        struXMLOutput.write();
        if (!AcsMain.hCNetSDK.NET_DVR_STDXMLConfig(lUserID, struXMLInput, struXMLOutput)) {
            int iErr = AcsMain.hCNetSDK.NET_DVR_GetLastError();
            System.err.println("NET_DVR_STDXMLConfig失败，错误号" + iErr);
            return null;
        } else {
            stringXMLOut.read();
            System.out.println("输出文本大小：" + struXMLOutput.dwReturnedXMLSize);
            //打印输出XML文本
            String strOutXML = new String(stringXMLOut.byValue.toString()).trim();
            struXMLStatus.read();
            String strStatus = new String(struXMLStatus.byValue.toString()).trim();
            return strOutXML;
        }
    }
}
