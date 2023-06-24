package org.dromara.manager.driver.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.DriverAttribute;
import org.dromara.manager.driver.domain.bo.DriverAttributeBo;
import org.dromara.manager.driver.domain.vo.DriverAttributeVo;

import java.util.Collection;
import java.util.List;

/**
 * 驱动属性Service接口
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
public interface IDriverAttributeService {

    /**
     * 查询驱动属性
     */
    DriverAttributeVo queryById(Long id);

    /**
     * 查询驱动属性列表
     */
    TableDataInfo<DriverAttributeVo> queryPageList(DriverAttributeBo bo, PageQuery pageQuery);

    /**
     * 查询驱动属性列表
     */
    List<DriverAttributeVo> queryList(DriverAttributeBo bo);

    /**
     * 新增驱动属性
     */
    Boolean insertByBo(DriverAttributeBo bo);

    /**
     * 修改驱动属性
     */
    Boolean updateByBo(DriverAttributeBo bo);

    /**
     * 校验并批量删除驱动属性信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过驱动id查询驱动属性
     *
     * @param driverId driverId
     * @return {@link List}<{@link DriverAttribute}>
     */
    List<DriverAttributeVo> selectByDriverId(Long driverId);

}
