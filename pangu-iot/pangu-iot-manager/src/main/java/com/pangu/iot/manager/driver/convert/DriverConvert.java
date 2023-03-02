package com.pangu.iot.manager.driver.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.domain.bo.DriverBO;
import com.pangu.iot.manager.driver.domain.vo.DriverConfigVO;
import com.pangu.iot.manager.driver.domain.vo.DriverVO;
import com.pangu.manager.api.domain.dto.DriverDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 协议驱动Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Mapper(componentModel = "spring")
public interface DriverConvert extends CommonConvert {

    Driver toEntity(DriverDTO dto);
    /**
     * DriverBO转换为DriverEntity
     *
     * @param bo DriverBO对象
     * @return driver
     */
    Driver toEntity(DriverBO bo);


    /**
     * DriverVO转换为DriverEntity
     *
     * @param  vo DriverVO对象
     * @return driver
     */
    @Mapping(source = "status", target = "enable", qualifiedByName = "boolToLong")
    Driver toEntity(DriverVO vo);

    /**
     * Driver转换为DriverBO
     *
     * @param  entity Driver对象
     * @return driverBO
     */
    DriverBO toBo(Driver entity);

    /**
     * Driver转换为DriverVO
     *
     * @param entity Driver
     * @return driverVO
     */
    @Mapping(source = "enable", target = "status", qualifiedByName = "longToBool")
    DriverVO toVo(Driver entity);

    /**
     * Driver转换为DriverVO
     *
     * @param entity Driver
     * @return driverVO
     */
    @Mapping(source = "enable", target = "status", qualifiedByName = "longToBool")
    DriverConfigVO toConfigVo(Driver entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<DriverBO>对象
     * @return List<Driver>
     */
    List<Driver> boListToEntityList(List<DriverBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<DriverVO>
     * @return List<Driver>
     */
    List<Driver> voListToEntityList(List<DriverVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<Driver>
     * @return List<DriverBO>
     */
    List<DriverBO> toBoList(List<Driver> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<Driver>
     * @return List<DriverVO>
     */
    List<DriverVO> toVoList(List<Driver> entities);

}
