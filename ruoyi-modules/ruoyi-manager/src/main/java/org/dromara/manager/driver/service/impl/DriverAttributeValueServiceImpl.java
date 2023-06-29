package org.dromara.manager.driver.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.DriverAttributeValue;
import org.dromara.manager.driver.domain.bo.DriverAttributeValueBo;
import org.dromara.manager.driver.domain.vo.DriverAttributeValueVo;
import org.dromara.manager.driver.mapper.DriverAttributeValueMapper;
import org.dromara.manager.driver.service.IDriverAttributeValueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 驱动属性值Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@RequiredArgsConstructor
@Service
public class DriverAttributeValueServiceImpl implements IDriverAttributeValueService {

    private final DriverAttributeValueMapper baseMapper;

    /**
     * 查询驱动属性值
     */
    @Override
    public DriverAttributeValueVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询驱动属性值列表
     */
    @Override
    public TableDataInfo<DriverAttributeValueVo> queryPageList(DriverAttributeValueBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DriverAttributeValue> lqw = buildQueryWrapper(bo);
        Page<DriverAttributeValueVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询驱动属性值列表
     */
    @Override
    public List<DriverAttributeValueVo> queryList(DriverAttributeValueBo bo) {
        LambdaQueryWrapper<DriverAttributeValue> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DriverAttributeValue> buildQueryWrapper(DriverAttributeValueBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DriverAttributeValue> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDriverAttributeId() != null, DriverAttributeValue::getDriverAttributeId, bo.getDriverAttributeId());
        lqw.eq(bo.getGatewayDeviceId() != null, DriverAttributeValue::getGatewayDeviceId, bo.getGatewayDeviceId());
        lqw.eq(StringUtils.isNotBlank(bo.getValue()), DriverAttributeValue::getValue, bo.getValue());
        return lqw;
    }

    /**
     * 新增驱动属性值
     */
    @Override
    public Boolean insertByBo(DriverAttributeValueBo bo) {
        DriverAttributeValue add = MapstructUtils.convert(bo, DriverAttributeValue.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 批量插入 DeviceAttributeValue
     *
     * @param bos bos
     * @return {@link Boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchInsertByBo(List<DriverAttributeValueBo> bos) {
        if (CollUtil.isEmpty(bos)) {
            return false;
        }
        List<DriverAttributeValue> list = MapstructUtils.convert(bos, DriverAttributeValue.class);
        return baseMapper.insertOrUpdateBatch(list);
    }

    /**
     * 修改驱动属性值
     */
    @Override
    public Boolean updateByBo(DriverAttributeValueBo bo) {
        DriverAttributeValue update = MapstructUtils.convert(bo, DriverAttributeValue.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DriverAttributeValue entity) {
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
     */
    @Override
    public Long countByAttributeId(Long attributeId) {
        return baseMapper.selectCount(Wrappers.lambdaQuery(DriverAttributeValue.class).eq(DriverAttributeValue::getDriverAttributeId, attributeId));
    }

    /**
     * 按设备id删除
     *
     * @param deviceId deviceId
     */
    @Override
    public Boolean deleteByDeviceId(Long deviceId) {
        return baseMapper.delete(Wrappers.lambdaQuery(DriverAttributeValue.class)
            .eq(DriverAttributeValue::getGatewayDeviceId, deviceId)) > 0;
    }

}
