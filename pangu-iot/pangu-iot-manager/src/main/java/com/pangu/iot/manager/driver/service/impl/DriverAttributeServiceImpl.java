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
import com.pangu.iot.manager.driver.domain.bo.DriverAttributeBO;
import com.pangu.iot.manager.driver.domain.vo.DriverAttributeVO;
import com.pangu.manager.api.domain.DriverAttribute;
import com.pangu.iot.manager.driver.mapper.DriverAttributeMapper;
import com.pangu.iot.manager.driver.service.IDriverAttributeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 驱动属性Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@RequiredArgsConstructor
@Service
public class DriverAttributeServiceImpl extends ServiceImpl<DriverAttributeMapper, DriverAttribute> implements IDriverAttributeService {

    private final DriverAttributeMapper baseMapper;

    @Override
    public List<DriverAttribute> selectByDriverId(Long id) {
        return baseMapper.selectList(Wrappers.lambdaQuery(DriverAttribute.class).eq(DriverAttribute::getDriverId, id));
    }

    /**
     * 查询驱动属性
     */
    @Override
    public DriverAttributeVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询驱动属性列表
     */
    @Override
    public TableDataInfo<DriverAttributeVO> queryPageList(DriverAttributeBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DriverAttribute> lqw = buildQueryWrapper(bo);
        Page<DriverAttributeVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询驱动属性列表
     */
    @Override
    public List<DriverAttributeVO> queryList(DriverAttributeBO bo) {
        LambdaQueryWrapper<DriverAttribute> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DriverAttribute> buildQueryWrapper(DriverAttributeBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DriverAttribute> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDriverId() != null, DriverAttribute::getDriverId, bo.getDriverId());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), DriverAttribute::getDisplayName, bo.getDisplayName());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DriverAttribute::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), DriverAttribute::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), DriverAttribute::getValue, bo.getValue());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), DriverAttribute::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增驱动属性
     */
    @Override
    public Boolean insertByBo(DriverAttributeBO bo) {
        DriverAttribute add = BeanUtil.toBean(bo, DriverAttribute.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改驱动属性
     */
    @Override
    public Boolean updateByBo(DriverAttributeBO bo) {
        DriverAttribute update = BeanUtil.toBean(bo, DriverAttribute.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DriverAttribute entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除驱动属性
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
