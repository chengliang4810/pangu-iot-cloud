package com.pangu.iot.manager.alarm.service.impl;

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
import com.pangu.iot.manager.alarm.domain.bo.ProblemBO;
import com.pangu.iot.manager.alarm.domain.vo.ProblemVO;
import com.pangu.iot.manager.alarm.domain.Problem;
import com.pangu.iot.manager.alarm.mapper.ProblemMapper;
import com.pangu.iot.manager.alarm.service.IProblemService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 告警记录Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-02-13
 */
@RequiredArgsConstructor
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements IProblemService {

    private final ProblemMapper baseMapper;

    /**
     * 查询告警记录
     */
    @Override
    public ProblemVO queryById(Long eventId){
        return baseMapper.selectVoById(eventId);
    }

    /**
     * 查询告警记录列表
     */
    @Override
    public TableDataInfo<ProblemVO> queryPageList(ProblemBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Problem> lqw = buildQueryWrapper(bo);
        Page<ProblemVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询告警记录列表
     */
    @Override
    public List<ProblemVO> queryList(ProblemBO bo) {
        LambdaQueryWrapper<Problem> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Problem> buildQueryWrapper(ProblemBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Problem> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getObjectId() != null, Problem::getObjectId, bo.getObjectId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Problem::getName, bo.getName());
        lqw.eq(bo.getAcknowledged() != null, Problem::getAcknowledged, bo.getAcknowledged());
        lqw.eq(bo.getClock() != null, Problem::getClock, bo.getClock());
        lqw.eq(bo.getRClock() != null, Problem::getRClock, bo.getRClock());
        lqw.eq(bo.getDeviceId() != null, Problem::getDeviceId, bo.getDeviceId());
        return lqw;
    }

    /**
     * 新增告警记录
     */
    @Override
    public Boolean insertByBo(ProblemBO bo) {
        Problem add = BeanUtil.toBean(bo, Problem.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setEventId(add.getEventId());
        }
        return flag;
    }

    /**
     * 修改告警记录
     */
    @Override
    public Boolean updateByBo(ProblemBO bo) {
        Problem update = BeanUtil.toBean(bo, Problem.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Problem entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除告警记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
