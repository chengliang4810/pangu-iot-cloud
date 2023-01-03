package com.pangu.manager.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pangu.common.core.constant.UserConstants;
import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.system.api.domain.SysDictData;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author chengliang4810
 */
public interface SysDictDataMapper extends BaseMapperPlus<SysDictDataMapper, SysDictData, SysDictData> {

    default List<SysDictData> selectDictDataByType(String dictType) {
        return selectList(
            new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getStatus, UserConstants.DICT_NORMAL)
                .eq(SysDictData::getDictType, dictType)
                .orderByAsc(SysDictData::getDictSort));
    }

}
