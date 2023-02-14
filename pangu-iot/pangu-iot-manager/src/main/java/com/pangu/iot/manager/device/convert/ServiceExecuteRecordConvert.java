package com.pangu.iot.manager.device.convert;

import java.util.List;

import com.pangu.iot.manager.device.domain.ServiceExecuteRecord;
import com.pangu.iot.manager.device.domain.vo.ServiceExecuteRecordVO;
import com.pangu.iot.manager.device.domain.bo.ServiceExecuteRecordBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 功能执行记录Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-14
 */
@Mapper(componentModel = "spring")
public interface ServiceExecuteRecordConvert extends CommonConvert {

    /**
     * ServiceExecuteRecordBO转换为ServiceExecuteRecordEntity
     *
     * @param bo ServiceExecuteRecordBO对象
     * @return serviceExecuteRecord
     */
    ServiceExecuteRecord toEntity(ServiceExecuteRecordBO bo);


    /**
     * ServiceExecuteRecordVO转换为ServiceExecuteRecordEntity
     *
     * @param  vo ServiceExecuteRecordVO对象
     * @return serviceExecuteRecord
     */
    ServiceExecuteRecord toEntity(ServiceExecuteRecordVO vo);

    /**
     * ServiceExecuteRecord转换为ServiceExecuteRecordBO
     *
     * @param  entity ServiceExecuteRecord对象
     * @return serviceExecuteRecordBO
     */
    ServiceExecuteRecordBO toBo(ServiceExecuteRecord entity);

    /**
     * ServiceExecuteRecord转换为ServiceExecuteRecordVO
     *
     * @param entity ServiceExecuteRecord
     * @return serviceExecuteRecordVO
     */
    ServiceExecuteRecordVO toVo(ServiceExecuteRecord entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ServiceExecuteRecordBO>对象
     * @return List<ServiceExecuteRecord>
     */
    List<ServiceExecuteRecord> boListToEntityList(List<ServiceExecuteRecordBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ServiceExecuteRecordVO>
     * @return List<ServiceExecuteRecord>
     */
    List<ServiceExecuteRecord> voListToEntityList(List<ServiceExecuteRecordVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ServiceExecuteRecord>
     * @return List<ServiceExecuteRecordBO>
     */
    List<ServiceExecuteRecordBO> toBoList(List<ServiceExecuteRecord> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ServiceExecuteRecord>
     * @return List<ServiceExecuteRecordVO>
     */
    List<ServiceExecuteRecordVO> toVoList(List<ServiceExecuteRecord> entities);

}
