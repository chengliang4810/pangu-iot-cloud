package com.pangu.iot.manager.driver.service;

import com.pangu.manager.api.domain.AttributeInfo;
import com.pangu.iot.manager.driver.domain.DriverInfo;
import com.pangu.iot.manager.driver.domain.bo.DriverInfoBatchBO;
import com.pangu.iot.manager.driver.domain.vo.DriverInfoVO;
import com.pangu.iot.manager.driver.domain.bo.DriverInfoBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.manager.api.domain.DriverAttribute;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 驱动属性配置信息Service接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
public interface IDriverInfoService extends IService<DriverInfo> {

    /**
     * 查询驱动属性配置信息
     */
    DriverInfoVO queryById(Long id);

    /**
     * 查询驱动属性配置信息列表
     */
    TableDataInfo<DriverInfoVO> queryPageList(DriverInfoBO bo, PageQuery pageQuery);

    /**
     * 查询驱动属性配置信息列表
     */
    List<DriverInfoVO> queryList(DriverInfoBO bo);

    /**
     * 修改驱动属性配置信息
     */
    Boolean insertByBo(DriverInfoBO bo);

    /**
     * 修改驱动属性配置信息
     */
    Boolean updateByBo(DriverInfoBO bo);

    /**
     * 校验并批量删除驱动属性配置信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据驱动属性配置 ID 查询
     *
     * @param driverAttributeId Driver Attribute Id
     * @return DriverInfo Array
     */
    List<DriverInfo>  selectByAttributeId(Long driverAttributeId);

    /**
     * 查询驱动配置值
     *
     * @param deviceId     设备id
     * @param attributeIds 属性id
     * @return {@link Map}<{@link Long}, {@link String}>
     */
    Map<Long, String> getDriverInfoValueMap(Long deviceId, List<Long> attributeIds);

    /**
     * 批量更新驱动程序信息
     *
     * @param bo 薄
     * @return int
     */
    Boolean batchUpdateDriverInfo(DriverInfoBatchBO bo);

    /**
     * 得到司机信息地图
     *
     * @param deviceIds          设备id
     * @param driverAttributeMap 驱动程序属性映射
     * @return {@link Map}<{@link Long}, {@link Map}<{@link Long}, {@link AttributeInfo}>>
     */
    Map<Long, Map<String, AttributeInfo>> getDriverInfoMap(Set<Long> deviceIds, Map<Long, DriverAttribute> driverAttributeMap);
}
