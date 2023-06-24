package org.dromara.manager.driver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.DriverAttribute;
import org.dromara.manager.driver.domain.bo.DriverAttributeBo;
import org.dromara.manager.driver.domain.vo.DriverAttributeVo;
import org.dromara.manager.driver.mapper.DriverAttributeMapper;
import org.dromara.manager.driver.service.IDriverAttributeService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 驱动属性Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@RequiredArgsConstructor
@Service
public class DriverAttributeServiceImpl implements IDriverAttributeService {

    private final DriverAttributeMapper baseMapper;

    /**
     * 查询驱动属性
     */
    @Override
    public DriverAttributeVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询驱动属性列表
     */
    @Override
    public TableDataInfo<DriverAttributeVo> queryPageList(DriverAttributeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DriverAttribute> lqw = buildQueryWrapper(bo);
        Page<DriverAttributeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询驱动属性列表
     */
    @Override
    public List<DriverAttributeVo> queryList(DriverAttributeBo bo) {
        LambdaQueryWrapper<DriverAttribute> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DriverAttribute> buildQueryWrapper(DriverAttributeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DriverAttribute> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDriverId() != null, DriverAttribute::getDriverId, bo.getDriverId());
        lqw.like(StringUtils.isNotBlank(bo.getAttributeName()), DriverAttribute::getAttributeName, bo.getAttributeName());
        lqw.eq(StringUtils.isNotBlank(bo.getAttributeType()), DriverAttribute::getAttributeType, bo.getAttributeType());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), DriverAttribute::getDisplayName, bo.getDisplayName());
        lqw.eq(StringUtils.isNotBlank(bo.getDefaultValue()), DriverAttribute::getDefaultValue, bo.getDefaultValue());
        lqw.eq(bo.getRequired() != null, DriverAttribute::getRequired, bo.getRequired());
        return lqw;
    }

    /**
     * 新增驱动属性
     */
    @Override
    public Boolean insertByBo(DriverAttributeBo bo) {
        DriverAttribute add = MapstructUtils.convert(bo, DriverAttribute.class);
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
    public Boolean updateByBo(DriverAttributeBo bo) {
        DriverAttribute update = MapstructUtils.convert(bo, DriverAttribute.class);
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

    /**
     * 通过驱动id查询驱动属性
     *
     * @param driverId driverId
     * @return {@link List}<{@link DriverAttribute}>
     */
    @Override
    public List<DriverAttributeVo> selectByDriverId(Long driverId) {
        return baseMapper.selectVoList(Wrappers.lambdaQuery(DriverAttribute.class).eq(DriverAttribute::getDriverId, driverId));
    }

}
