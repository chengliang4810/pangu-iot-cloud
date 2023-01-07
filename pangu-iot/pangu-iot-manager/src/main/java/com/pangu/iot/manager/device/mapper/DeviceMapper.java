package com.pangu.iot.manager.device.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.iot.manager.device.domain.Device;
import com.pangu.iot.manager.device.domain.vo.DeviceListVO;
import com.pangu.iot.manager.device.domain.vo.DeviceVO;
import org.apache.ibatis.annotations.Param;

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

}
