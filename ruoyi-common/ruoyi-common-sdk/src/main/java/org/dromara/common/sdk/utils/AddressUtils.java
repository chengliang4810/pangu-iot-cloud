package org.dromara.common.sdk.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.constant.Constants;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.json.utils.JsonUtils;

import java.net.InetAddress;

/**
 * 获取地址类
 *
 * @author chengliang4810
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressUtils {

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    public static String getRealAddressByIP(String ip) {
        String address = UNKNOWN;
        if (StringUtils.isBlank(ip)) {
            return address;
        }
        // 内网不查询
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : HtmlUtil.cleanHtmlTag(ip);
        if (NetUtil.isInnerIP(ip)) {
            return "内网IP";
        }
//        if (PanGuConfig.isAddressEnabled()) {
        try {
            String rspStr = HttpUtil.createGet(IP_URL)
                .body("ip=" + ip + "&json=true", Constants.GBK)
                .execute()
                .body();
            if (StringUtils.isEmpty(rspStr)) {
                log.error("获取地理位置异常 {}", ip);
                return UNKNOWN;
            }
            Dict obj = JsonUtils.parseMap(rspStr);
            String region = obj.getStr("pro");
            String city = obj.getStr("city");
            return String.format("%s %s", region, city);
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", ip);
        }
//        }
        return address;
    }

    /**
     * 获取当前主机的 Local Host
     *
     * @return String
     */
    public static String localHost() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
