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
import com.pangu.iot.manager.driver.domain.bo.PointInfoBO;
import com.pangu.iot.manager.driver.domain.vo.PointInfoVO;
import com.pangu.iot.manager.driver.domain.PointInfo;
import com.pangu.iot.manager.driver.mapper.PointInfoMapper;
import com.pangu.iot.manager.driver.service.IPointInfoService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 点位属性配置信息Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-28
 */
@RequiredArgsConstructor
@Service
public class PointInfoServiceImpl extends ServiceImpl<PointInfoMapper, PointInfo> implements IPointInfoService {

    private final PointInfoMapper baseMapper;

    @Override
    public List<PointInfo> selectByAttributeId(Long pointAttributeId) {
        return baseMapper.selectList(Wrappers.lambdaQuery(PointInfo.class).eq(PointInfo::getPointAttributeId, pointAttributeId));
    }

    /**
     * 查询点位属性配置信息
     */
    @Override
    public PointInfoVO queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询点位属性配置信息列表
     */
    @Override
    public TableDataInfo<PointInfoVO> queryPageList(PointInfoBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PointInfo> lqw = buildQueryWrapper(bo);
        Page<PointInfoVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询点位属性配置信息列表
     */
    @Override
    public List<PointInfoVO> queryList(PointInfoBO bo) {
        LambdaQueryWrapper<PointInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PointInfo> buildQueryWrapper(PointInfoBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PointInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPointAttributeId() != null, PointInfo::getPointAttributeId, bo.getPointAttributeId());
        lqw.eq(bo.getDeviceId() != null, PointInfo::getDeviceId, bo.getDeviceId());
        lqw.eq(bo.getPointId() != null, PointInfo::getPointId, bo.getPointId());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), PointInfo::getValue, bo.getValue());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), PointInfo::getDescription, bo.getDescription());
        return lqw;
    }

    /**
     * 新增点位属性配置信息
     */
    @Override
    public Boolean insertByBo(PointInfoBO bo) {
        PointInfo add = BeanUtil.toBean(bo, PointInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改点位属性配置信息
     */
    @Override
    public Boolean updateByBo(PointInfoBO bo) {
        PointInfo update = BeanUtil.toBean(bo, PointInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PointInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除点位属性配置信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
