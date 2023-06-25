package org.dromara.manager.driver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.PointAttribute;
import org.dromara.manager.driver.domain.bo.PointAttributeBo;
import org.dromara.manager.driver.domain.vo.PointAttributeVo;
import org.dromara.manager.driver.mapper.PointAttributeMapper;
import org.dromara.manager.driver.service.IPointAttributeService;
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
public class PointAttributeServiceImpl implements IPointAttributeService {

    private final PointAttributeMapper baseMapper;

    /**
     * 查询驱动属性
     */
    @Override
    public PointAttributeVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询驱动属性列表
     */
    @Override
    public TableDataInfo<PointAttributeVo> queryPageList(PointAttributeBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PointAttribute> lqw = buildQueryWrapper(bo);
        Page<PointAttributeVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询驱动属性列表
     */
    @Override
    public List<PointAttributeVo> queryList(PointAttributeBo bo) {
        LambdaQueryWrapper<PointAttribute> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PointAttribute> buildQueryWrapper(PointAttributeBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PointAttribute> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDriverId() != null, PointAttribute::getDriverId, bo.getDriverId());
        lqw.like(StringUtils.isNotBlank(bo.getAttributeName()), PointAttribute::getAttributeName, bo.getAttributeName());
        lqw.eq(StringUtils.isNotBlank(bo.getAttributeType()), PointAttribute::getAttributeType, bo.getAttributeType());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), PointAttribute::getDisplayName, bo.getDisplayName());
        lqw.eq(StringUtils.isNotBlank(bo.getDefaultValue()), PointAttribute::getDefaultValue, bo.getDefaultValue());
        lqw.eq(bo.getRequired() != null, PointAttribute::getRequired, bo.getRequired());
        return lqw;
    }

    /**
     * 新增驱动属性
     */
    @Override
    public Boolean insertByBo(PointAttributeBo bo) {
        PointAttribute add = MapstructUtils.convert(bo, PointAttribute.class);
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
    public Boolean updateByBo(PointAttributeBo bo) {
        PointAttribute update = MapstructUtils.convert(bo, PointAttribute.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PointAttribute entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除驱动属性
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 选择通过驱动程序id
     *
     * @param driverId 司机身份证
     * @return {@link List}<{@link PointAttribute}>
     */
    @Override
    public List<PointAttributeVo> selectByDriverId(Long driverId) {
        return baseMapper.selectVoList(Wrappers.<PointAttribute>lambdaQuery().eq(PointAttribute::getDriverId, driverId));
    }

}
