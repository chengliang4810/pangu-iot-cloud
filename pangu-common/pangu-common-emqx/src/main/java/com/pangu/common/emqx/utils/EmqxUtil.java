package com.pangu.common.emqx.utils;

import com.pangu.common.core.utils.SpringUtils;
import com.pangu.common.emqx.doamin.EmqxClient;

public class EmqxUtil {

    public static EmqxClient getClient() {
        return SpringUtils.getBean(EmqxClient.class);
    }

}
