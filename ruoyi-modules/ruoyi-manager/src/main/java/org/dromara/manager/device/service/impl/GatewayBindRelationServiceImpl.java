package org.dromara.manager.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.device.domain.GatewayBindRelation;
import org.dromara.manager.device.domain.bo.GatewayBindRelationBo;
import org.dromara.manager.device.domain.vo.GatewayBindRelationVo;
import org.dromara.manager.device.mapper.GatewayBindRelationMapper;
import org.dromara.manager.device.service.IGatewayBindRelationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 网关设备绑定子设备关系Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-26
 */
@RequiredArgsConstructor
@Service
public class GatewayBindRelationServiceImpl implements IGatewayBindRelationService {

    private final GatewayBindRelationMapper baseMapper;

    /**
     * 查询网关设备绑定子设备关系
     */
    @Override
    public GatewayBindRelationVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询网关设备绑定子设备关系列表
     */
    @Override
    public TableDataInfo<GatewayBindRelationVo> queryPageList(GatewayBindRelationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<GatewayBindRelation> lqw = buildQueryWrapper(bo);
        Page<GatewayBindRelationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询网关设备绑定子设备关系列表
     */
    @Override
    public List<GatewayBindRelationVo> queryList(GatewayBindRelationBo bo) {
        LambdaQueryWrapper<GatewayBindRelation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<GatewayBindRelation> buildQueryWrapper(GatewayBindRelationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<GatewayBindRelation> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getGatewayDeviceId() != null, GatewayBindRelation::getGatewayDeviceId, bo.getGatewayDeviceId());
        lqw.eq(bo.getDeviceId() != null, GatewayBindRelation::getDeviceId, bo.getDeviceId());
        return lqw;
    }

    /**
     * 新增网关设备绑定子设备关系
     */
    @Override
    public Boolean insertByBo(GatewayBindRelationBo bo) {
        GatewayBindRelation add = MapstructUtils.convert(bo, GatewayBindRelation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改网关设备绑定子设备关系
     */
    @Override
    public Boolean updateByBo(GatewayBindRelationBo bo) {
        GatewayBindRelation update = MapstructUtils.convert(bo, GatewayBindRelation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(GatewayBindRelation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除网关设备绑定子设备关系
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 存在子设备
     *
     * @param deviceId      设备id
     * @param childDeviceId 子设备id
     * @return {@link Boolean}
     */
    @Override
    public Boolean existChildDevice(Long deviceId, Long childDeviceId) {
        return baseMapper.exists(Wrappers.lambdaQuery(GatewayBindRelation.class)
                .eq(GatewayBindRelation::getGatewayDeviceId, deviceId)
                .eq(GatewayBindRelation::getDeviceId, childDeviceId));
    }

    /**
     * 统计子设备数量
     *
     * @param deviceId
     */
    @Override
    public Long countChildDevice(Long deviceId) {
        if (deviceId == null) {
            return 0L;
        }
        return baseMapper.selectCount(Wrappers.lambdaQuery(GatewayBindRelation.class)
                .eq(GatewayBindRelation::getGatewayDeviceId, deviceId));
    }

    /**
     * 按设备id删除
     *
     * @param id id
     * @return {@link Boolean}
     */
    @Override
    public Boolean deleteByDeviceId(Long id) {
        return baseMapper.delete(Wrappers.lambdaQuery(GatewayBindRelation.class)
                .eq(GatewayBindRelation::getDeviceId, id)) > 0;
    }

}
