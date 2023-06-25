package org.dromara.manager.driver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.DriverApplication;
import org.dromara.manager.driver.domain.bo.DriverApplicationBo;
import org.dromara.manager.driver.domain.vo.DriverApplicationVo;
import org.dromara.manager.driver.mapper.DriverApplicationMapper;
import org.dromara.manager.driver.service.IDriverApplicationService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 驱动应用Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
@RequiredArgsConstructor
@Service
public class DriverApplicationServiceImpl implements IDriverApplicationService {

    private final DriverApplicationMapper baseMapper;

    /**
     * 查询驱动应用
     */
    @Override
    public DriverApplicationVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询驱动应用列表
     */
    @Override
    public TableDataInfo<DriverApplicationVo> queryPageList(DriverApplicationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DriverApplication> lqw = buildQueryWrapper(bo);
        Page<DriverApplicationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询驱动应用列表
     */
    @Override
    public List<DriverApplicationVo> queryList(DriverApplicationBo bo) {
        LambdaQueryWrapper<DriverApplication> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DriverApplication> buildQueryWrapper(DriverApplicationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DriverApplication> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDriverId() != null, DriverApplication::getDriverId, bo.getDriverId());
        lqw.like(StringUtils.isNotBlank(bo.getApplicationName()), DriverApplication::getApplicationName, bo.getApplicationName());
        lqw.eq(StringUtils.isNotBlank(bo.getHost()), DriverApplication::getHost, bo.getHost());
        lqw.eq(bo.getPort() != null, DriverApplication::getPort, bo.getPort());
        return lqw;
    }

    /**
     * 查询一个
     *
     * @param bo 薄
     * @return {@link DriverApplicationVo}
     */
    @Override
    public DriverApplicationVo queryOne(DriverApplicationBo bo) {
        return baseMapper.selectVoOne(buildQueryWrapper(bo).last("LIMIT 1"));
    }

    /**
     * 查询是否存在
     *
     * @param bo 薄
     * @return {@link DriverApplicationVo}
     */
    @Override
    public Boolean exist(DriverApplicationBo bo) {
        return baseMapper.exists(buildQueryWrapper(bo).last("LIMIT 1"));
    }

    /**
     * 新增驱动应用
     */
    @Override
    public Boolean insertByBo(DriverApplicationBo bo) {
        DriverApplication add = MapstructUtils.convert(bo, DriverApplication.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改驱动应用
     */
    @Override
    public Boolean updateByBo(DriverApplicationBo bo) {
        DriverApplication update = MapstructUtils.convert(bo, DriverApplication.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DriverApplication entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除驱动应用
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }

        // TODO 关闭驱动应用，删除驱动应用数据

        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
