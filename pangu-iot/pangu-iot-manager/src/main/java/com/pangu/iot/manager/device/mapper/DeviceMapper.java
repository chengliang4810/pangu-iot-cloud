package com.pangu.iot.manager.device.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.manager.api.domain.Device;
import com.pangu.iot.manager.device.domain.vo.DeviceDetailVO;
import com.pangu.iot.manager.device.domain.vo.DeviceListVO;
import com.pangu.iot.manager.device.domain.vo.DeviceVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 设备Mapper接口
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
public interface DeviceMapper extends BaseMapperPlus<DeviceMapper, Device, DeviceVO> {


    /**
     * 查询设备列表组合数据
     *
     * @param ew 查询条件
     * @return {@link DeviceListVO}
     */
    Page<DeviceListVO> selectVoPageList(IPage<Device> page,@Param(Constants.WRAPPER) Wrapper ew);

    /**
     * 通过id 获取设备详情信息
     *
     * @param id id
     * @return {@link DeviceDetailVO}
     */
    DeviceDetailVO detailById(Long id);

    /**
     * 查询设备id 根据code
     *
     * @param code 编码
     * @return {@link Long}
     */
    @Select("select id from iot_device where status = 1 and code = #{code}")
    Long selectDeviceIdByCode(String code);

}
