package com.pangu.iot.manager.driver.service;

import com.pangu.iot.manager.driver.domain.DriverService;
import com.pangu.iot.manager.driver.domain.vo.DriverServiceVO;
import com.pangu.iot.manager.driver.domain.bo.DriverServiceBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 驱动服务Service接口
 *
 * @author chengliang4810
 * @date 2023-03-01
 */
public interface IDriverServiceService extends IService<DriverService> {

    /**
     * 查询驱动服务
     */
    DriverServiceVO queryById(Long id);

    /**
     * 查询驱动服务列表
     */
    TableDataInfo<DriverServiceVO> queryPageList(DriverServiceBO bo, PageQuery pageQuery);

    /**
     * 查询驱动服务列表
     */
    List<DriverServiceVO> queryList(DriverServiceBO bo);

    /**
     * 修改驱动服务
     */
    Boolean insertByBo(DriverServiceBO bo);

    /**
     * 修改驱动服务
     */
    Boolean updateByBo(DriverServiceBO bo);

    /**
     * 校验并批量删除驱动服务信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
