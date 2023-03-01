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
import com.pangu.iot.manager.driver.domain.bo.DriverServiceBO;
import com.pangu.iot.manager.driver.domain.vo.DriverServiceVO;
import com.pangu.iot.manager.driver.domain.DriverService;
import com.pangu.iot.manager.driver.mapper.DriverServiceMapper;
import com.pangu.iot.manager.driver.service.IDriverServiceService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 驱动服务Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-03-01
 */
@RequiredArgsConstructor
@Service
public class DriverServiceServiceImpl extends ServiceImpl<DriverServiceMapper, DriverService> implements IDriverServiceService {

    private final DriverServiceMapper baseMapper;

    /**
     * 查询驱动服务
     */
    @Override
    public DriverServiceVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询驱动服务列表
     */
    @Override
    public TableDataInfo<DriverServiceVO> queryPageList(DriverServiceBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DriverService> lqw = buildQueryWrapper(bo);
        Page<DriverServiceVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询驱动服务列表
     */
    @Override
    public List<DriverServiceVO> queryList(DriverServiceBO bo) {
        LambdaQueryWrapper<DriverService> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DriverService> buildQueryWrapper(DriverServiceBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DriverService> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDriverId() != null, DriverService::getDriverId, bo.getDriverId());
        lqw.like(StringUtils.isNotBlank(bo.getServiceName()), DriverService::getServiceName, bo.getServiceName());
        lqw.eq(StringUtils.isNotBlank(bo.getHost()), DriverService::getHost, bo.getHost());
        lqw.eq(bo.getPort() != null, DriverService::getPort, bo.getPort());
        return lqw;
    }

    /**
     * 新增驱动服务
     */
    @Override
    public Boolean insertByBo(DriverServiceBO bo) {
        DriverService add = BeanUtil.toBean(bo, DriverService.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改驱动服务
     */
    @Override
    public Boolean updateByBo(DriverServiceBO bo) {
        DriverService update = BeanUtil.toBean(bo, DriverService.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DriverService entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除驱动服务
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
