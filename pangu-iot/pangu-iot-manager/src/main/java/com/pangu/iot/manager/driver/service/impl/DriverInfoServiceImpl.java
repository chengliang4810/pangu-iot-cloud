package com.pangu.iot.manager.driver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.driver.domain.DriverInfo;
import com.pangu.iot.manager.driver.domain.bo.DriverInfoBO;
import com.pangu.iot.manager.driver.domain.bo.DriverInfoBatchBO;
import com.pangu.iot.manager.driver.domain.vo.DriverInfoVO;
import com.pangu.iot.manager.driver.mapper.DriverInfoMapper;
import com.pangu.iot.manager.driver.service.IDriverInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 驱动属性配置信息Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@RequiredArgsConstructor
@Service
public class DriverInfoServiceImpl extends ServiceImpl<DriverInfoMapper, DriverInfo> implements IDriverInfoService {

    private final DriverInfoMapper baseMapper;

    /**
     * 批量更新驱动程序信息
     *
     * @param bo 薄
     * @return int
     */
    @Override
    public Boolean batchUpdateDriverInfo(DriverInfoBatchBO bo) {

        Long deviceId = bo.getDeviceId();
        Map<Long, String> attributeValue = bo.getAttributeValue();
        List<DriverInfo> driverInfoList = new ArrayList<DriverInfo>(attributeValue.size());

        attributeValue.forEach((attributeId,value) -> {
            // 查询驱动配置信息
            DriverInfo dbDriverInfo = baseMapper.selectOne(Wrappers.lambdaQuery(DriverInfo.class)
                    .eq(DriverInfo::getDeviceId, deviceId)
                    .eq(DriverInfo::getDriverAttributeId, attributeId)
                    .last("limit 1")
            );

            // 构建驱动配置信息
            DriverInfo driverInfo = new DriverInfo();
            driverInfo.setDeviceId(deviceId);
            driverInfo.setDriverAttributeId(attributeId);
            driverInfo.setValue(value);
            // 数据库存在则更新，不存在则插入 通过主键判断
            if (ObjectUtil.isNotNull(dbDriverInfo)){
                driverInfo.setId(driverInfo.getId());
            }
            driverInfoList.add(driverInfo);
        });

        return baseMapper.insertOrUpdateBatch(driverInfoList);
    }


    /**
     * 查询驱动配置值
     *
     * @param deviceId     设备id
     * @param attributeIds 属性id
     * @return {@link Map}<{@link Long}, {@link String}>
     */
    @Override
    public Map<Long, String> getDriverInfoValueMap(Long deviceId, List<Long> attributeIds) {
        List<DriverInfo> driverInfos = baseMapper.selectList(Wrappers.lambdaQuery(DriverInfo.class)
                .eq(DriverInfo::getDeviceId, deviceId)
                .in(DriverInfo::getDriverAttributeId, attributeIds));
        if (CollectionUtil.isEmpty(driverInfos)) {
            return Collections.emptyMap();
        }
        return driverInfos.stream().collect(Collectors.toMap(DriverInfo::getDriverAttributeId, DriverInfo::getValue));
    }

    @Override
    public List<DriverInfo> selectByAttributeId(Long driverAttributeId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(DriverInfo.class).eq(DriverInfo::getDriverAttributeId, driverAttributeId));
    }

    /**
     * 查询驱动属性配置信息
     */
    @Override
    public DriverInfoVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询驱动属性配置信息列表
     */
    @Override
    public TableDataInfo<DriverInfoVO> queryPageList(DriverInfoBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DriverInfo> lqw = buildQueryWrapper(bo);
        Page<DriverInfoVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询驱动属性配置信息列表
     */
    @Override
    public List<DriverInfoVO> queryList(DriverInfoBO bo) {
        LambdaQueryWrapper<DriverInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DriverInfo> buildQueryWrapper(DriverInfoBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DriverInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDriverAttributeId() != null, DriverInfo::getDriverAttributeId, bo.getDriverAttributeId());
        lqw.eq(bo.getDeviceId() != null, DriverInfo::getDeviceId, bo.getDeviceId());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), DriverInfo::getValue, bo.getValue());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), DriverInfo::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增驱动属性配置信息
     */
    @Override
    public Boolean insertByBo(DriverInfoBO bo) {
        DriverInfo add = BeanUtil.toBean(bo, DriverInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改驱动属性配置信息
     */
    @Override
    public Boolean updateByBo(DriverInfoBO bo) {
        DriverInfo update = BeanUtil.toBean(bo, DriverInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DriverInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除驱动属性配置信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}