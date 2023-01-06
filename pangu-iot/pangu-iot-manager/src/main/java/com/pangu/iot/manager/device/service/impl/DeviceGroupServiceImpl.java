package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.common.zabbix.service.HostGroupService;
import com.pangu.iot.manager.device.convert.DeviceGroupConvert;
import com.pangu.iot.manager.device.domain.Device;
import com.pangu.iot.manager.device.domain.DeviceGroup;
import com.pangu.iot.manager.device.domain.bo.DeviceGroupBO;
import com.pangu.iot.manager.device.domain.vo.DeviceGroupVO;
import com.pangu.iot.manager.device.mapper.DeviceGroupMapper;
import com.pangu.iot.manager.device.service.IDeviceGroupService;
import com.pangu.iot.manager.device.service.IDeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备分组Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@RequiredArgsConstructor
@Service
public class DeviceGroupServiceImpl implements IDeviceGroupService {

    private final IDeviceService deviceService;
    private final DeviceGroupMapper baseMapper;
    private final HostGroupService hostGroupService;
    private final DeviceGroupConvert deviceGroupConvert;

    /**
     * 查询设备分组
     */
    @Override
    public DeviceGroupVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设备分组列表
     */
    @Override
    public TableDataInfo<DeviceGroupVO> queryPageList(DeviceGroupBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DeviceGroup> lqw = buildQueryWrapper(bo);
        Page<DeviceGroupVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设备分组列表
     */
    @Override
    public List<DeviceGroupVO> queryList(DeviceGroupBO bo) {
        LambdaQueryWrapper<DeviceGroup> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DeviceGroup> buildQueryWrapper(DeviceGroupBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DeviceGroup> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), DeviceGroup::getName, bo.getName());
        return lqw;
    }

    /**
     * 根据GroupName检验设备组名称
     *
     * @param groupName 组名称
     * @return {@link Boolean}
     */
    private void checkByGroupName(String groupName){
        Long count = baseMapper.selectCount(Wrappers.lambdaQuery(DeviceGroup.class).eq(DeviceGroup::getName, groupName));
        Assert.isLessOrEqualZero(count, "设备组 {} 已存在", groupName);
    }

    /**
     * 新增设备分组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(DeviceGroupBO bo) {

        // 判断设备组是否重复
        checkByGroupName(bo.getName());

        // 生成主键
        Long id = IdUtil.getSnowflake().nextId();
        DeviceGroup deviceGroup = deviceGroupConvert.toEntity(bo);
        deviceGroup.setId(id);

        // 创建zbx并关联主机组
        String zbxId = hostGroupService.createHostGroup(id.toString());
        deviceGroup.setZbxId(zbxId);

        // 入库
        boolean flag = baseMapper.insert(deviceGroup) > 0;
        if (flag) {
            bo.setId(deviceGroup.getId());
        }
        return flag;
    }

    /**
     * 修改设备分组
     */
    @Override
    public Boolean updateByBo(DeviceGroupBO bo) {
        // 检查分组信息
        DeviceGroup deviceGroup = baseMapper.selectById(bo.getId());
        Assert.notNull(deviceGroup, "设备分组不存在");
        // 检查分组名称是否重复
        if (!deviceGroup.getName().equals(bo.getName())) {
            checkByGroupName(bo.getName());
        }

        DeviceGroup update = BeanUtil.toBean(bo, DeviceGroup.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DeviceGroup entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设备分组
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {

        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }

        // 是否存在
        List<DeviceGroup> deviceGroupList = baseMapper.selectBatchIds(ids);
        Assert.notEmpty(deviceGroupList, "产品分组不存在");

        //检查是否关联设备
        long count = deviceService.count(Wrappers.lambdaQuery(Device.class).in(Device::getGroupId, ids));
        Assert.isLessOrEqualZero(count, "设备组绑定了设备,请先解除绑定!");

        // 关联删除zabbix 分组
        List<String> zbxIds = deviceGroupList.stream().map(DeviceGroup::getZbxId).collect(Collectors.toList());
        hostGroupService.hostGroupDelete(zbxIds);

        // 入库
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
