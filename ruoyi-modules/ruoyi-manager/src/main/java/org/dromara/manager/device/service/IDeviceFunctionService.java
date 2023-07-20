package org.dromara.manager.device.service;

import org.dromara.manager.device.domain.DeviceFunction;
import org.dromara.manager.device.domain.vo.DeviceFunctionVo;
import org.dromara.manager.device.domain.bo.DeviceFunctionBo;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 设备功能Service接口
 *
 * @author chengliang4810
 * @date 2023-07-20
 */
public interface IDeviceFunctionService {

    /**
     * 查询设备功能
     */
    DeviceFunctionVo queryById(Long id);

    /**
     * 查询设备功能列表
     */
    TableDataInfo<DeviceFunctionVo> queryPageList(DeviceFunctionBo bo, PageQuery pageQuery);

    /**
     * 查询设备功能列表
     */
    List<DeviceFunctionVo> queryList(DeviceFunctionBo bo);

    /**
     * 新增设备功能
     */
    Boolean insertByBo(DeviceFunctionBo bo);

    /**
     * 修改设备功能
     */
    Boolean updateByBo(DeviceFunctionBo bo);

    /**
     * 校验并批量删除设备功能信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
