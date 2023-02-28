package com.pangu.iot.manager.driver.service;

import com.pangu.iot.manager.driver.domain.PointInfo;
import com.pangu.iot.manager.driver.domain.vo.PointInfoVO;
import com.pangu.iot.manager.driver.domain.bo.PointInfoBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 点位属性配置信息Service接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
public interface IPointInfoService extends IService<PointInfo> {

    /**
     * 查询点位属性配置信息
     */
    PointInfoVO queryById(Long id);

    /**
     * 查询点位属性配置信息列表
     */
    TableDataInfo<PointInfoVO> queryPageList(PointInfoBO bo, PageQuery pageQuery);

    /**
     * 查询点位属性配置信息列表
     */
    List<PointInfoVO> queryList(PointInfoBO bo);

    /**
     * 修改点位属性配置信息
     */
    Boolean insertByBo(PointInfoBO bo);

    /**
     * 修改点位属性配置信息
     */
    Boolean updateByBo(PointInfoBO bo);

    /**
     * 校验并批量删除点位属性配置信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 根据位号配置信息 ID 查询
     *
     * @param pointAttributeId Point Attribute Id
     * @return PointInfo Array
     */
    List<PointInfo> selectByAttributeId(Long pointAttributeId);
}
