package com.pangu.iot.manager.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.ServiceExecuteRecord;
import com.pangu.iot.manager.device.domain.bo.ServiceExecuteRecordBO;
import com.pangu.iot.manager.device.domain.vo.ServiceExecuteRecordVO;

import java.util.Collection;
import java.util.List;

/**
 * 功能执行记录Service接口
 *
 * @author chengliang4810
 * @date 2023-02-14
 */
public interface IServiceExecuteRecordService extends IService<ServiceExecuteRecord> {

    /**
     * 查询功能执行记录
     */
    ServiceExecuteRecordVO queryById(Long id);

    /**
     * 查询功能执行记录列表
     */
    TableDataInfo<ServiceExecuteRecordVO> queryPageList(ServiceExecuteRecordBO bo, PageQuery pageQuery);

    /**
     * 查询功能执行记录列表
     */
    List<ServiceExecuteRecordVO> queryList(ServiceExecuteRecordBO bo);

    /**
     * 修改功能执行记录
     */
    Boolean insertByBo(ServiceExecuteRecordBO bo);

    /**
     * 修改功能执行记录
     */
    Boolean updateByBo(ServiceExecuteRecordBO bo);

    /**
     * 校验并批量删除功能执行记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
