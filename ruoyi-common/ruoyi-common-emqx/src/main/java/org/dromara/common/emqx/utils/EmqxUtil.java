package org.dromara.common.emqx.utils;

import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.emqx.doamin.EmqxClient;

public class EmqxUtil {

    public static EmqxClient getClient() {
        return SpringUtils.getBean(EmqxClient.class);
    }

}
