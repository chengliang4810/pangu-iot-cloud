package org.dromara.manager.thing.service;

import org.dromara.manager.thing.domain.bo.ThingFunctionBo;
import org.dromara.manager.thing.domain.vo.ThingFunctionVo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 物模型功能Service接口
 *
 * @author chengliang4810
 * @date 2023-07-20
 */
public interface IThingFunctionService {

    /**
     * 查询物模型功能
     */
    ThingFunctionVo queryById(Long id);

    /**
     * 查询物模型功能列表
     */
    TableDataInfo<ThingFunctionVo> queryPageList(ThingFunctionBo bo, PageQuery pageQuery);

    /**
     * 查询物模型功能列表
     */
    List<ThingFunctionVo> queryList(ThingFunctionBo bo);

    /**
     * 新增物模型功能
     */
    Boolean insertByBo(ThingFunctionBo bo);

    /**
     * 修改物模型功能
     */
    Boolean updateByBo(ThingFunctionBo bo);

    /**
     * 校验并批量删除物模型功能信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
