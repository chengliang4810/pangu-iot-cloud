package com.pangu.iot.manager.driver.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pangu.iot.manager.driver.domain.bo.DriverBO;
import com.pangu.iot.manager.driver.domain.vo.DriverVO;
import com.pangu.iot.manager.driver.domain.Driver;
import com.pangu.iot.manager.driver.mapper.DriverMapper;
import com.pangu.iot.manager.driver.service.IDriverService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 协议驱动Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@Service
@RequiredArgsConstructor
public class DriverServiceImpl extends ServiceImpl<DriverMapper, Driver> implements IDriverService {

    private final DriverMapper baseMapper;

    @Override
    public Driver selectByServiceName(String serviceName) {
        return baseMapper.selectOne(Wrappers.lambdaQuery(Driver.class).eq(Driver::getServiceName, serviceName).last("limit 1"));
    }

    /**
     * 查询协议驱动
     */
    @Override
    public DriverVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询协议驱动列表
     */
    @Override
    public TableDataInfo<DriverVO> queryPageList(DriverBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Driver> lqw = buildQueryWrapper(bo);
        Page<DriverVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询协议驱动列表
     */
    @Override
    public List<DriverVO> queryList(DriverBO bo) {
        LambdaQueryWrapper<Driver> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Driver> buildQueryWrapper(DriverBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Driver> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), Driver::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), Driver::getDisplayName, bo.getDisplayName());
        lqw.like(StringUtils.isNotBlank(bo.getServiceName()), Driver::getServiceName, bo.getServiceName());
        lqw.eq(StringUtils.isNotBlank(bo.getHost()), Driver::getHost, bo.getHost());
        lqw.eq(bo.getPort() != null, Driver::getPort, bo.getPort());
        lqw.eq(bo.getEnable() != null, Driver::getEnable, bo.getEnable());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), Driver::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增协议驱动
     */
    @Override
    public Boolean insertByBo(DriverBO bo) {
        Driver add = BeanUtil.toBean(bo, Driver.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改协议驱动
     */
    @Override
    public Boolean updateByBo(DriverBO bo) {
        Driver update = BeanUtil.toBean(bo, Driver.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Driver entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除协议驱动
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
