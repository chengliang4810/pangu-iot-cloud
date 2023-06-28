package org.dromara.manager.driver.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.bo.DriverAttributeValueBo;
import org.dromara.manager.driver.domain.vo.DriverAttributeValueVo;

import java.util.Collection;
import java.util.List;

/**
 * 驱动属性值Service接口
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
public interface IDriverAttributeValueService {

    /**
     * 查询驱动属性值
     */
    DriverAttributeValueVo queryById(Long id);

    /**
     * 查询驱动属性值列表
     */
    TableDataInfo<DriverAttributeValueVo> queryPageList(DriverAttributeValueBo bo, PageQuery pageQuery);

    /**
     * 查询驱动属性值列表
     */
    List<DriverAttributeValueVo> queryList(DriverAttributeValueBo bo);

    /**
     * 新增驱动属性值
     */
    Boolean insertByBo(DriverAttributeValueBo bo);

    /**
     * 修改驱动属性值
     */
    Boolean updateByBo(DriverAttributeValueBo bo);

    /**
     * 校验并批量删除驱动属性值信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 选择通过属性id
     *
     * @param attributeId 属性id
     */
    Long countByAttributeId(Long attributeId);

    /**
     * 按设备id删除
     *
     * @param id id
     */
    Boolean deleteByDeviceId(Long id);
}
