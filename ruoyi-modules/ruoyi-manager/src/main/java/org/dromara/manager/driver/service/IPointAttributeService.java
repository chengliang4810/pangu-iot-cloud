package org.dromara.manager.driver.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.bo.PointAttributeBo;
import org.dromara.manager.driver.domain.vo.PointAttributeVo;

import java.util.Collection;
import java.util.List;

/**
 * 驱动属性Service接口
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
public interface IPointAttributeService {

    /**
     * 查询驱动属性
     */
    PointAttributeVo queryById(Long id);

    /**
     * 查询驱动属性列表
     */
    TableDataInfo<PointAttributeVo> queryPageList(PointAttributeBo bo, PageQuery pageQuery);

    /**
     * 查询驱动属性列表
     */
    List<PointAttributeVo> queryList(PointAttributeBo bo);

    /**
     * 新增驱动属性
     */
    Boolean insertByBo(PointAttributeBo bo);

    /**
     * 修改驱动属性
     */
    Boolean updateByBo(PointAttributeBo bo);

    /**
     * 校验并批量删除驱动属性信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
