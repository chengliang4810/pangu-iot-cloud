package org.dromara.manager.driver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.Driver;
import org.dromara.manager.driver.domain.bo.DriverBo;
import org.dromara.manager.driver.domain.vo.DriverVo;
import org.dromara.manager.driver.mapper.DriverMapper;
import org.dromara.manager.driver.service.IDriverService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 驱动Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-16
 */
@RequiredArgsConstructor
@Service
public class DriverServiceImpl implements IDriverService {

    private final DriverMapper baseMapper;

    /**
     * 查询驱动
     */
    @Override
    public DriverVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询驱动列表
     */
    @Override
    public TableDataInfo<DriverVo> queryPageList(DriverBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Driver> lqw = buildQueryWrapper(bo);
        Page<DriverVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询驱动列表
     */
    @Override
    public List<DriverVo> queryList(DriverBo bo) {
        LambdaQueryWrapper<Driver> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Driver> buildQueryWrapper(DriverBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Driver> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), Driver::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getDisplayName()), Driver::getDisplayName, bo.getDisplayName());
        lqw.eq(bo.getEnable() != null, Driver::getEnable, bo.getEnable());
        return lqw;
    }

    /**
     * 新增驱动
     */
    @Override
    public Boolean insertByBo(DriverBo bo) {
        Driver add = MapstructUtils.convert(bo, Driver.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改驱动
     */
    @Override
    public Boolean updateByBo(DriverBo bo) {
        Driver update = MapstructUtils.convert(bo, Driver.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Driver entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 根据code查询驱动信息
     *
     * @param driverCode 驱动代码
     * @return {@link Driver}
     */
    @Override
    public Driver queryByCode(String driverCode) {
        if (StringUtils.isBlank(driverCode)) {
            return null;
        }
        return baseMapper.selectOne(Wrappers.lambdaQuery(Driver.class).eq(Driver::getCode, driverCode).last("limit 1"));
    }

    /**
     * 批量删除驱动
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 查询父设备的驱动信息
     *
     * @param deviceId 设备id
     * @return {@link List}<{@link DriverVo}>
     */
    @Override
    public List<DriverVo> queryParentDeviceDriver(Long deviceId) {
        return baseMapper.selectParentDeviceDriver(deviceId);
    }

}
