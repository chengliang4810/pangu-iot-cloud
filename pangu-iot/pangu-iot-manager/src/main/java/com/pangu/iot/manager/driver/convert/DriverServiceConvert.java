package com.pangu.iot.manager.driver.convert;

import java.util.List;

import com.pangu.iot.manager.driver.domain.DriverService;
import com.pangu.iot.manager.driver.domain.vo.DriverServiceVO;
import com.pangu.iot.manager.driver.domain.bo.DriverServiceBO;
import com.pangu.common.core.convert.CommonConvert;
import org.mapstruct.Mapper;

/**
 * 驱动服务Convert接口
 *
 * @author chengliang4810
 * @date 2023-03-01
 */
@Mapper(componentModel = "spring")
public interface DriverServiceConvert extends CommonConvert {

    /**
     * DriverServiceBO转换为DriverServiceEntity
     *
     * @param bo DriverServiceBO对象
     * @return driverService
     */
    DriverService toEntity(DriverServiceBO bo);


    /**
     * DriverServiceVO转换为DriverServiceEntity
     *
     * @param  vo DriverServiceVO对象
     * @return driverService
     */
    DriverService toEntity(DriverServiceVO vo);

    /**
     * DriverService转换为DriverServiceBO
     *
     * @param  entity DriverService对象
     * @return driverServiceBO
     */
    DriverServiceBO toBo(DriverService entity);

    /**
     * DriverService转换为DriverServiceVO
     *
     * @param entity DriverService
     * @return driverServiceVO
     */
    DriverServiceVO toVo(DriverService entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<DriverServiceBO>对象
     * @return List<DriverService>
     */
    List<DriverService> boListToEntityList(List<DriverServiceBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<DriverServiceVO>
     * @return List<DriverService>
     */
    List<DriverService> voListToEntityList(List<DriverServiceVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<DriverService>
     * @return List<DriverServiceBO>
     */
    List<DriverServiceBO> toBoList(List<DriverService> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<DriverService>
     * @return List<DriverServiceVO>
     */
    List<DriverServiceVO> toVoList(List<DriverService> entities);

}
