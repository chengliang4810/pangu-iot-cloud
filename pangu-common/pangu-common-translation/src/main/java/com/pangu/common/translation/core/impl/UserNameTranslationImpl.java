package com.pangu.common.translation.core.impl;

import com.pangu.common.translation.annotation.TranslationType;
import com.pangu.common.translation.constant.TransConstant;
import com.pangu.common.translation.core.TranslationInterface;
import com.pangu.system.api.RemoteUserService;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;

/**
 * 用户名翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.USER_ID_TO_NAME)
public class UserNameTranslationImpl implements TranslationInterface<String> {

    @DubboReference
    private RemoteUserService remoteUserService;

    public String translation(Object key, String other) {
        return remoteUserService.selectUserNameById((Long) key);
    }
}
