package com.pangu.common.translation.core.impl;

import com.pangu.common.core.service.DictService;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.translation.annotation.TranslationType;
import com.pangu.common.translation.constant.TransConstant;
import com.pangu.common.translation.core.TranslationInterface;
import lombok.AllArgsConstructor;

/**
 * 字典翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.DICT_TYPE_TO_LABEL)
public class DictTypeTranslationImpl implements TranslationInterface<String> {

    private final DictService dictService;

    public String translation(Object key, String other) {
        if (key instanceof String && StringUtils.isNotBlank(other)) {
            return dictService.getDictLabel(other, key.toString());
        }
        return null;
    }
}
