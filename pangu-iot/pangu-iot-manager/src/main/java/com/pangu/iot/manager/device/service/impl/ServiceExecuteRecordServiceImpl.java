package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.iot.manager.device.domain.ServiceExecuteRecord;
import com.pangu.iot.manager.device.domain.bo.ServiceExecuteRecordBO;
import com.pangu.iot.manager.device.domain.vo.DeviceLogVO;
import com.pangu.iot.manager.device.domain.vo.ServiceExecuteRecordVO;
import com.pangu.iot.manager.device.mapper.ServiceExecuteRecordMapper;
import com.pangu.iot.manager.device.service.IServiceExecuteRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 功能执行记录Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-14
 */
@RequiredArgsConstructor
@Service
public class ServiceExecuteRecordServiceImpl extends ServiceImpl<ServiceExecuteRecordMapper, ServiceExecuteRecord> implements IServiceExecuteRecordService {

    private final ServiceExecuteRecordMapper baseMapper;

    /**
     * 查询功能执行记录
     */
    @Override
    public ServiceExecuteRecordVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询功能执行记录列表
     */
    @Override
    public TableDataInfo<ServiceExecuteRecordVO> queryPageList(ServiceExecuteRecordBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ServiceExecuteRecord> lqw = buildQueryWrapper(bo);
        Page<ServiceExecuteRecordVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询功能执行记录列表
     */
    @Override
    public List<ServiceExecuteRecordVO> queryList(ServiceExecuteRecordBO bo) {
        LambdaQueryWrapper<ServiceExecuteRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ServiceExecuteRecord> buildQueryWrapper(ServiceExecuteRecordBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ServiceExecuteRecord> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getServiceName()), ServiceExecuteRecord::getServiceName, bo.getServiceName());
        lqw.eq(StringUtils.isNotBlank(bo.getParam()), ServiceExecuteRecord::getParam, bo.getParam());
        lqw.eq(bo.getDeviceId() != null, ServiceExecuteRecord::getDeviceId, bo.getDeviceId());
        lqw.eq(bo.getExecuteType() != null, ServiceExecuteRecord::getExecuteType, bo.getExecuteType());
        lqw.eq(StringUtils.isNotBlank(bo.getExecuteUser()), ServiceExecuteRecord::getExecuteUser, bo.getExecuteUser());
        lqw.eq(bo.getExecuteRuleId() != null, ServiceExecuteRecord::getExecuteRuleId, bo.getExecuteRuleId());
        return lqw;
    }

    /**
     * 新增功能执行记录
     */
    @Override
    public Boolean insertByBo(ServiceExecuteRecordBO bo) {
        ServiceExecuteRecord add = BeanUtil.toBean(bo, ServiceExecuteRecord.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改功能执行记录
     */
    @Override
    public Boolean updateByBo(ServiceExecuteRecordBO bo) {
        ServiceExecuteRecord update = BeanUtil.toBean(bo, ServiceExecuteRecord.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ServiceExecuteRecord entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除功能执行记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
