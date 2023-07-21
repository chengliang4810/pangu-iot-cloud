package org.dromara.manager.thing.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.thing.domain.bo.ThingAttributeBo;
import org.dromara.manager.thing.domain.vo.ThingAttributeVo;

import java.util.Collection;
import java.util.List;

/**
 * 物模型属性Service接口
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
public interface IThingAttributeService {

    /**
     * 查询物模型属性
     */
    ThingAttributeVo queryByCodeAndIdentifier(String deviceCode, String identifier);

    /**
     * 查询物模型属性
     */
    ThingAttributeVo queryById(Long id);

    /**
     * 查询物模型属性列表
     */
    TableDataInfo<ThingAttributeVo> queryPageList(ThingAttributeBo bo, PageQuery pageQuery);

    /**
     * 查询物模型属性列表
     */
    List<ThingAttributeVo> queryList(ThingAttributeBo bo);

    /**
     * 通过设备代码查询列表
     *
     * @param deviceCode 设备代码
     * @return {@link List}<{@link ThingAttributeVo}>
     */
    List<ThingAttributeVo> queryListByDeviceCode(String deviceCode);

    /**
     * 新增物模型属性
     */
    Boolean insertByBo(ThingAttributeBo bo);

    /**
     * 修改物模型属性
     */
    Boolean updateByBo(ThingAttributeBo bo);

    /**
     * 校验并批量删除物模型属性信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询设备id 属性对应的列表
     *
     * @param bo
     * @return {@link List}<{@link ThingAttributeVo}>
     */
    List<ThingAttributeVo> queryListByProductIdAndDeviceId(ThingAttributeBo bo);

    /**
     * 根据设备id仅删除物模型属性
     *
     * @param deviceId id
     * @return {@link Boolean}
     */
    Boolean deleteByDeviceId(Long deviceId);

    /**
     * 按产品id删除
     *
     * @param productId 产品id
     * @return {@link Boolean}
     */
    Boolean deleteByProductId(Long productId);

    /**
     * 查询设备id列表
     *
     * @param deviceId   设备id
     * @param isRealTime 是真实时间
     * @return {@link List}<{@link ThingAttributeVo}>
     */
    List<ThingAttributeVo> queryListByDeviceId(Long deviceId, Boolean isRealTime);
}
