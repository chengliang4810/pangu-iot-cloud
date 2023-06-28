package org.dromara.manager.device.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.device.domain.bo.DeviceAttributeBo;
import org.dromara.manager.device.domain.vo.DeviceAttributeVo;

import java.util.Collection;
import java.util.List;

/**
 * 设备属性Service接口
 *
 * @author chengliang4810
 * @date 2023-06-27
 */
public interface IDeviceAttributeService {

    /**
     * 查询设备属性
     */
    DeviceAttributeVo queryById(Long id);

    /**
     * 查询设备属性列表
     */
    TableDataInfo<DeviceAttributeVo> queryPageList(DeviceAttributeBo bo, PageQuery pageQuery);

    /**
     * 查询设备属性列表
     */
    List<DeviceAttributeVo> queryList(DeviceAttributeBo bo);

    /**
     * 新增设备属性
     */
    Boolean insertByBo(DeviceAttributeBo bo);

    /**
     * 修改设备属性
     */
    Boolean updateByBo(DeviceAttributeBo bo);

    /**
     * 校验并批量删除设备属性信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 查询设备id 属性对应的列表
     *
     * @param bo
     * @return {@link List}<{@link DeviceAttributeVo}>
     */
    List<DeviceAttributeVo> queryListByProductIdAndDeviceId(DeviceAttributeBo bo);
}
