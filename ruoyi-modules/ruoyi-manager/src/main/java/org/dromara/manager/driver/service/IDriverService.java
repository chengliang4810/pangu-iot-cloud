package org.dromara.manager.driver.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.Driver;
import org.dromara.manager.driver.domain.bo.DriverBo;
import org.dromara.manager.driver.domain.vo.DriverVo;

import java.util.Collection;
import java.util.List;

/**
 * 驱动Service接口
 *
 * @author chengliang4810
 * @date 2023-06-16
 */
public interface IDriverService {

    /**
     * 查询驱动
     */
    DriverVo queryById(Long id);

    /**
     * 查询驱动列表
     */
    TableDataInfo<DriverVo> queryPageList(DriverBo bo, PageQuery pageQuery);

    /**
     * 查询驱动列表
     */
    List<DriverVo> queryList(DriverBo bo);

    /**
     * 新增驱动
     */
    Boolean insertByBo(DriverBo bo);

    /**
     * 修改驱动
     */
    Boolean updateByBo(DriverBo bo);

    /**
     * 校验并批量删除驱动信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据code查询驱动信息
     *
     * @param driverCode 驱动代码
     * @return {@link Driver}
     */
    Driver queryByCode(String driverCode);

    /**
     * 查询父设备的驱动信息
     *
     * @param deviceId 设备id
     * @return {@link List}<{@link DriverVo}>
     */
    List<DriverVo> queryParentDeviceDriver(Long deviceId);
}
