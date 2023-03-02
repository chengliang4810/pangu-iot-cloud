package com.pangu.iot.manager.driver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.driver.domain.bo.PointAttributeBO;
import com.pangu.iot.manager.driver.domain.vo.DriverPointConfigVO;
import com.pangu.iot.manager.driver.domain.vo.PointAttributeVO;
import com.pangu.manager.api.domain.PointAttribute;

import java.util.Collection;
import java.util.List;

/**
 * 点位属性Service接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
public interface IPointAttributeService extends IService<PointAttribute> {

    /**
     * 查询点位属性
     */
    PointAttributeVO queryById(Long id);

    /**
     * 查询点位属性列表
     */
    TableDataInfo<PointAttributeVO> queryPageList(PointAttributeBO bo, PageQuery pageQuery);

    /**
     * 查询点位属性列表
     */
    List<PointAttributeVO> queryList(PointAttributeBO bo);

    /**
     * 修改点位属性
     */
    Boolean insertByBo(PointAttributeBO bo);

    /**
     * 修改点位属性
     */
    Boolean updateByBo(PointAttributeBO bo);

    /**
     * 校验并批量删除点位属性信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据驱动 ID 查询
     *
     * @param driverId
     * @return PointAttribute Array
     */
    List<PointAttribute> selectByDriverId(Long driverId);

    /**
     * 司机配置设备id
     *
     * @param deviceId    设备id
     * @param attributeId 设备属性id
     * @return {@link List}<{@link DriverPointConfigVO}>
     */
    List<DriverPointConfigVO> getDriverPointConfigByDeviceId(Long deviceId, Long attributeId);
}
