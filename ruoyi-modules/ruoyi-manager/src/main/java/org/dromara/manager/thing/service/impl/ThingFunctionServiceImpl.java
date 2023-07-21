package org.dromara.manager.thing.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.manager.thing.domain.vo.ThingFunctionVo;
import org.springframework.stereotype.Service;
import org.dromara.manager.thing.domain.bo.ThingFunctionBo;
import org.dromara.manager.thing.domain.ThingFunction;
import org.dromara.manager.thing.mapper.ThingFunctionMapper;
import org.dromara.manager.thing.service.IThingFunctionService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 物模型功能Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-07-20
 */
@RequiredArgsConstructor
@Service
public class ThingFunctionServiceImpl implements IThingFunctionService {

    private final ThingFunctionMapper baseMapper;

    /**
     * 查询物模型功能
     */
    @Override
    public ThingFunctionVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询物模型功能列表
     */
    @Override
    public TableDataInfo<ThingFunctionVo> queryPageList(ThingFunctionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ThingFunction> lqw = buildQueryWrapper(bo);
        Page<ThingFunctionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询物模型功能列表
     */
    @Override
    public List<ThingFunctionVo> queryList(ThingFunctionBo bo) {
        LambdaQueryWrapper<ThingFunction> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ThingFunction> buildQueryWrapper(ThingFunctionBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ThingFunction> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, ThingFunction::getProductId, bo.getProductId());
        lqw.eq(bo.getDeviceId() != null, ThingFunction::getDeviceId, bo.getDeviceId());
        lqw.eq(bo.getDriverId() != null, ThingFunction::getDriverId, bo.getDriverId());
        lqw.eq(bo.getFunctionStatusAttribute() != null, ThingFunction::getFunctionStatusAttribute, bo.getFunctionStatusAttribute());
        lqw.like(StringUtils.isNotBlank(bo.getFunctionName()), ThingFunction::getFunctionName, bo.getFunctionName());
        lqw.like(StringUtils.isNotBlank(bo.getIdentifier()), ThingFunction::getIdentifier, bo.getIdentifier());
        lqw.eq(StringUtils.isNotBlank(bo.getDataType()), ThingFunction::getDataType, bo.getDataType());
        lqw.eq(bo.getAsync() != null, ThingFunction::getAsync, bo.getAsync());
        return lqw;
    }

    /**
     * 新增物模型功能
     */
    @Override
    public Boolean insertByBo(ThingFunctionBo bo) {
        ThingFunction add = MapstructUtils.convert(bo, ThingFunction.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改物模型功能
     */
    @Override
    public Boolean updateByBo(ThingFunctionBo bo) {
        ThingFunction update = MapstructUtils.convert(bo, ThingFunction.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ThingFunction entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除物模型功能
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
