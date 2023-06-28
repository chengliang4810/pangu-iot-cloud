package org.dromara.manager.driver.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.bo.PointAttributeValueBo;
import org.dromara.manager.driver.domain.vo.PointAttributeValueVo;

import java.util.Collection;
import java.util.List;

/**
 * 驱动属性值Service接口
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
public interface IPointAttributeValueService {

    /**
     * 查询驱动属性值
     */
    PointAttributeValueVo queryById(Long id);

    /**
     * 查询驱动属性值列表
     */
    TableDataInfo<PointAttributeValueVo> queryPageList(PointAttributeValueBo bo, PageQuery pageQuery);

    /**
     * 查询驱动属性值列表
     */
    List<PointAttributeValueVo> queryList(PointAttributeValueBo bo);

    /**
     * 新增驱动属性值
     */
    Boolean insertByBo(PointAttributeValueBo bo);

    /**
     * 修改驱动属性值
     */
    Boolean updateByBo(PointAttributeValueBo bo);

    /**
     * 校验并批量删除驱动属性值信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 选择通过属性id
     *
     * @param attributeId 属性id
     * @return {@link PointAttributeValueVo}
     */
    Boolean existByAttributeId(Long attributeId);

    /**
     * 通过设备id pointId 查询配置信息
     *
     * @param deviceId 设备id
     * @param pointId  点id
     * @return {@link List}<{@link PointAttributeValueVo}>
     */
    List<PointAttributeValueVo> queryByDeviceIdAndPointId(Long deviceId, Long pointId);

    /**
     * 按设备id删除
     *
     * @param deviceId 设备id
     * @return {@link Boolean}
     */
    Boolean deleteByDeviceId(Long deviceId);
}
