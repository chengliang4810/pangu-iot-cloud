package org.dromara.manager.device.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.device.domain.bo.GatewayBindRelationBo;
import org.dromara.manager.device.domain.vo.GatewayBindRelationVo;

import java.util.Collection;
import java.util.List;

/**
 * 网关设备绑定子设备关系Service接口
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
public interface IGatewayBindRelationService {

    /**
     * 查询网关设备绑定子设备关系
     */
    GatewayBindRelationVo queryById(Long id);

    /**
     * 查询网关设备绑定子设备关系列表
     */
    TableDataInfo<GatewayBindRelationVo> queryPageList(GatewayBindRelationBo bo, PageQuery pageQuery);

    /**
     * 查询网关设备绑定子设备关系列表
     */
    List<GatewayBindRelationVo> queryList(GatewayBindRelationBo bo);

    /**
     * 新增网关设备绑定子设备关系
     */
    Boolean insertByBo(GatewayBindRelationBo bo);

    /**
     * 修改网关设备绑定子设备关系
     */
    Boolean updateByBo(GatewayBindRelationBo bo);

    /**
     * 校验并批量删除网关设备绑定子设备关系信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 存在子设备
     *
     * @param deviceId      设备id
     * @param childDeviceId 子设备id
     * @return {@link Boolean}
     */
    Boolean existChildDevice(Long deviceId, Long childDeviceId);


    /**
     * 统计子设备数量
     */
    Long countChildDevice(Long deviceId);

    /**
     * 按设备id删除
     *
     * @param id id
     * @return {@link Boolean}
     */
    Boolean deleteByDeviceId(Long id);
}
