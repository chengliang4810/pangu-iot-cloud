package com.pangu.iot.manager.driver.service;

import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.domain.vo.DriverVO;
import com.pangu.iot.manager.driver.domain.bo.DriverBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 协议驱动Service接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
public interface IDriverService extends IService<Driver> {

    /**
     * 查询协议驱动
     */
    DriverVO queryById(Long id);

    /**
     * 查询协议驱动列表
     */
    TableDataInfo<DriverVO> queryPageList(DriverBO bo, PageQuery pageQuery);

    /**
     * 查询协议驱动列表
     */
    List<DriverVO> queryList(DriverBO bo);

    /**
     * 修改协议驱动
     */
    Boolean insertByBo(DriverBO bo);

    /**
     * 修改协议驱动
     */
    Boolean updateByBo(DriverBO bo);

    /**
     * 校验并批量删除协议驱动信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据服务名称查询驱动
     *
     * @param serviceName 服务名称
     * @return {@link Driver}
     */
    Driver selectByServiceName(String serviceName);
}
