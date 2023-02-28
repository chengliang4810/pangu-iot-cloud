package com.pangu.iot.manager.driver.service;

import com.pangu.manager.api.domain.DriverAttribute;
import com.pangu.iot.manager.driver.domain.vo.DriverAttributeVO;
import com.pangu.iot.manager.driver.domain.bo.DriverAttributeBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 驱动属性Service接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
public interface IDriverAttributeService extends IService<DriverAttribute> {

    /**
     * 查询驱动属性
     */
    DriverAttributeVO queryById(Long id);

    /**
     * 查询驱动属性列表
     */
    TableDataInfo<DriverAttributeVO> queryPageList(DriverAttributeBO bo, PageQuery pageQuery);

    /**
     * 查询驱动属性列表
     */
    List<DriverAttributeVO> queryList(DriverAttributeBO bo);

    /**
     * 修改驱动属性
     */
    Boolean insertByBo(DriverAttributeBO bo);

    /**
     * 修改驱动属性
     */
    Boolean updateByBo(DriverAttributeBO bo);

    /**
     * 校验并批量删除驱动属性信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<DriverAttribute> selectByDriverId(Long id);
}
