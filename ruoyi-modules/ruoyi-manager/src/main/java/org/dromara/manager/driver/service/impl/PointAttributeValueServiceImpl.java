package org.dromara.manager.driver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.PointAttributeValue;
import org.dromara.manager.driver.domain.bo.BatchPointAttributeValueBo;
import org.dromara.manager.driver.domain.bo.BatchPointAttributeValueItem;
import org.dromara.manager.driver.domain.bo.PointAttributeValueBo;
import org.dromara.manager.driver.domain.vo.PointAttributeValueVo;
import org.dromara.manager.driver.mapper.PointAttributeValueMapper;
import org.dromara.manager.driver.service.IPointAttributeValueService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 驱动属性值Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@RequiredArgsConstructor
@Service
public class PointAttributeValueServiceImpl implements IPointAttributeValueService {

    private final PointAttributeValueMapper baseMapper;


    /**
     * 通过设备id pointId 查询配置信息
     *
     * @param deviceId 设备id
     * @param pointId  点id
     * @return {@link List}<{@link PointAttributeValueVo}>
     */
    @Override
    public List<PointAttributeValueVo> queryByDeviceIdAndPointId(Long deviceId, Long pointId) {
        if (null == deviceId || null == pointId){
            return Collections.emptyList();
        }
        return baseMapper.selectVoList(Wrappers.lambdaQuery(PointAttributeValue.class)
            .eq(PointAttributeValue::getDeviceId, deviceId).eq(PointAttributeValue::getDeviceAttributeId, pointId));
    }

    /**
     * 查询驱动属性值
     */
    @Override
    public PointAttributeValueVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询驱动属性值列表
     */
    @Override
    public TableDataInfo<PointAttributeValueVo> queryPageList(PointAttributeValueBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PointAttributeValue> lqw = buildQueryWrapper(bo);
        Page<PointAttributeValueVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询驱动属性值列表
     */
    @Override
    public List<PointAttributeValueVo> queryList(PointAttributeValueBo bo) {
        LambdaQueryWrapper<PointAttributeValue> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<PointAttributeValue> buildQueryWrapper(PointAttributeValueBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<PointAttributeValue> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPointAttributeId() != null, PointAttributeValue::getPointAttributeId, bo.getPointAttributeId());
        lqw.eq(bo.getDeviceId() != null, PointAttributeValue::getDeviceId, bo.getDeviceId());
        lqw.eq(bo.getDeviceAttributeId() != null, PointAttributeValue::getDeviceAttributeId, bo.getDeviceAttributeId());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), PointAttributeValue::getValue, bo.getValue());
        return lqw;
    }

    /**
     * 新增驱动属性值
     */
    @Override
    public Boolean insertByBo(PointAttributeValueBo bo) {
        PointAttributeValue add = MapstructUtils.convert(bo, PointAttributeValue.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 新增/修改驱动属性值
     * @param bo
     * @return {@link Boolean}
     */
    @Override
    public Boolean insertByBo(BatchPointAttributeValueBo bo) {
        List<BatchPointAttributeValueItem> valueItems = bo.getPointAttributeConfig();
        List<PointAttributeValue> batchList = new ArrayList<>(valueItems.size());
        valueItems.forEach(item -> {
            PointAttributeValue add = MapstructUtils.convert(item, PointAttributeValue.class);
            add.setPointAttributeId(item.getPointAttributeId());
            add.setValue(item.getValue());
            add.setId(item.getId());
            batchList.add(add);
        });
        return baseMapper.insertOrUpdateBatch(batchList);
    }

    /**
     * 修改驱动属性值
     */
    @Override
    public Boolean updateByBo(PointAttributeValueBo bo) {
        PointAttributeValue update = MapstructUtils.convert(bo, PointAttributeValue.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(PointAttributeValue entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除驱动属性值
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 选择通过属性id
     *
     * @param attributeId 属性id
     * @return {@link PointAttributeValueVo}
     */
    @Override
    public Boolean existByAttributeId(Long attributeId) {
        return baseMapper.exists(Wrappers.<PointAttributeValue>lambdaQuery().eq(PointAttributeValue::getPointAttributeId, attributeId));
    }

    /**
     * 按设备id删除
     *
     * @param deviceId 设备id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteByDeviceId(Long deviceId) {
        return baseMapper.delete(Wrappers.<PointAttributeValue>lambdaQuery()
            .eq(PointAttributeValue::getDeviceId, deviceId)) > 0;
    }

}
