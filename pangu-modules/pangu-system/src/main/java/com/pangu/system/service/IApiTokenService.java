package com.pangu.system.service;

import com.pangu.system.domain.ApiToken;
import com.pangu.system.domain.vo.ApiTokenVO;
import com.pangu.system.domain.bo.ApiTokenBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 三方授权Service接口
 *
 * @author chengliang4810
 * @date 2023-03-14
 */
public interface IApiTokenService extends IService<ApiToken> {

    /**
     * 查询三方授权
     */
    ApiTokenVO queryById(Long id);

    /**
     * 查询三方授权列表
     */
    TableDataInfo<ApiTokenVO> queryPageList(ApiTokenBO bo, PageQuery pageQuery);

    /**
     * 查询三方授权列表
     */
    List<ApiTokenVO> queryList(ApiTokenBO bo);

    /**
     * 创建Token
     *
     * @param bo 薄
     * @return {@link String} token值
     */
    String insertByBo(ApiTokenBO bo);

    /**
     * 修改三方授权
     */
    Boolean updateByBo(ApiTokenBO bo);

    /**
     * 校验并批量删除三方授权信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    String resetToken(Long id);

}
