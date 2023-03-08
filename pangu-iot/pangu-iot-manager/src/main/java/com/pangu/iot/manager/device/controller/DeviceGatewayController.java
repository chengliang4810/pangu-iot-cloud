package com.pangu.iot.manager.device.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.pangu.common.core.domain.R;
import com.pangu.common.log.annotation.Log;
import com.pangu.common.log.enums.BusinessType;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.bo.DeviceBO;
import com.pangu.iot.manager.device.domain.bo.DeviceGatewayBindBo;
import com.pangu.iot.manager.device.domain.vo.DeviceListVO;
import com.pangu.iot.manager.device.service.IDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/device/gateway")
public class DeviceGatewayController {

    private final IDeviceService deviceService;

    /**
     * 分页查询网关子设备列表
     */
    @SaCheckPermission("manager:device:list")
    @GetMapping("/child/list")
    public TableDataInfo<DeviceListVO> list(DeviceBO bo, PageQuery pageQuery) {
        return deviceService.queryPageList(bo, pageQuery);
    }

    /**
     * 网关绑定设备的ID列表
     *
     * @param id id
     * @return {@link R}<{@link List}<{@link Long}>>
     */
    @GetMapping("/bind/{id}")
    @SaCheckPermission("manager:device:query")
    public R<List<Long>> gatewayDevice(@PathVariable Long id){
        return R.ok(deviceService.queryGatewayDeviceBindIds(id));
    }

    /**
     * 设备绑定到网关
     *
     * @param deviceGatewayBindBo 网关设备绑定博
     * @return {@link R}<{@link Boolean}>
     */
    @PutMapping("/bind")
    @SaCheckPermission("manager:device:edit")
    @Log(title = "设备", businessType = BusinessType.UPDATE)
    public R<Boolean> bindGateway(@RequestBody DeviceGatewayBindBo deviceGatewayBindBo){
        return R.ok(deviceService.bindGatewayDevice(deviceGatewayBindBo));
    }

}
