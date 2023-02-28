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
import com.pangu.iot.manager.driver.domain.bo.PointAttributeBO;
import com.pangu.iot.manager.driver.domain.vo.PointAttributeVO;
import com.pangu.manager.api.domain.PointAttribute;
import com.pangu.iot.manager.driver.mapper.PointAttributeMapper;
import com.pangu.iot.manager.driver.service.IPointAttributeService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 点位属性Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@RequiredArgsConstructor
@Service
public class PointAttributeServiceImpl extends ServiceImpl<PointAttributeMapper, PointAttribute> implements IPointAttributeService {

    private final PointAttributeMapper baseMapper;

    @Override
    public List<PointAttribute> selectByDriverId(Long driverId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(PointAttribute.class).eq(PointAttribute::getDriverId, driverId));
    }

    /**
     * 查询点位属性
     */
    @Override
    public PointAttributeVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询点位属性列表
     */
    @Override
    public TableDataInfo<PointAttributeVO> queryPageList(PointAttributeBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PointAttribute> lqw = buildQueryWrapper(bo);
        Page<PointAttributeVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询点位属性列表
     */
    @Override
    public List<PointAttributeVO> queryList(PointAttributeBO bo) {
        LambdaQueryWrapper<PointAttribute> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PointAttribute> buildQueryWrapper(PointAttributeBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PointAttribute> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDriverId() != null, PointAttribute::getDriverId, bo.getDriverId());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), PointAttribute::getDisplayName, bo.getDisplayName());
        lqw.like(StringUtils.isNotBlank(bo.getName()), PointAttribute::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), PointAttribute::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), PointAttribute::getValue, bo.getValue());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), PointAttribute::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增点位属性
     */
    @Override
    public Boolean insertByBo(PointAttributeBO bo) {
        PointAttribute add = BeanUtil.toBean(bo, PointAttribute.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改点位属性
     */
    @Override
    public Boolean updateByBo(PointAttributeBO bo) {
        PointAttribute update = BeanUtil.toBean(bo, PointAttribute.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PointAttribute entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除点位属性
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
