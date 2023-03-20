package com.pangu.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.core.utils.StringUtils;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.system.domain.ApiToken;
import com.pangu.system.domain.bo.ApiTokenBO;
import com.pangu.system.domain.vo.ApiTokenVO;
import com.pangu.system.mapper.ApiTokenMapper;
import com.pangu.system.service.IApiTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 三方授权Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-03-14
 */
@RequiredArgsConstructor
@Service
public class ApiTokenServiceImpl extends ServiceImpl<ApiTokenMapper, ApiToken> implements IApiTokenService {

    private final ApiTokenMapper baseMapper;

    /**
     * 查询三方授权
     */
    @Override
    public ApiTokenVO queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询三方授权列表
     */
    @Override
    public TableDataInfo<ApiTokenVO> queryPageList(ApiTokenBO bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ApiToken> lqw = buildQueryWrapper(bo);
        Page<ApiTokenVO> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询三方授权列表
     */
    @Override
    public List<ApiTokenVO> queryList(ApiTokenBO bo) {
        LambdaQueryWrapper<ApiToken> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ApiToken> buildQueryWrapper(ApiTokenBO bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ApiToken> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getName()), ApiToken::getName, bo.getName());
        lqw.eq(bo.getExpirationTime() != null, ApiToken::getExpirationTime, bo.getExpirationTime());
        lqw.eq(bo.getStatus() != null, ApiToken::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增三方授权
     */
    @Override
    public String insertByBo(ApiTokenBO bo) {

        ApiToken add = BeanUtil.toBean(bo, ApiToken.class);
        // 生成token
        add.setToken(IdUtil.fastSimpleUUID());

        validEntityBeforeSave(add);

        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return add.getToken();
    }

    /**
     * 修改三方授权
     */
    @Override
    public Boolean updateByBo(ApiTokenBO bo) {
        ApiToken update = BeanUtil.toBean(bo, ApiToken.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public String resetToken(Long id) {

        ApiToken apiToken = baseMapper.selectById(id);
        Assert.notNull(apiToken, "Token记录不存在");
        // 生成token
        String token = IdUtil.fastSimpleUUID();
        apiToken.setToken(token);

        // 重置token
        baseMapper.updateById(apiToken);
        return token;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(ApiToken entity) {
        //TODO 做一些数据校验,如唯一约束
        String token = entity.getToken();
        Long count = baseMapper.selectCount(Wrappers.<ApiToken>lambdaQuery().eq(ApiToken::getToken, token).ne(entity.getId() != null, ApiToken::getId, entity.getId()));
        Assert.isLessOrEqualZero(count, "token已存在, 请重新生成");
    }

    /**
     * 批量删除三方授权
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        boolean result = baseMapper.deleteBatchIds(ids) > 0;
        Assert.isTrue(result, "删除失败");
        // 退出Token登录
        ids.forEach(StpUtil::logout);
        return result;
    }
}
