package com.pangu.resource.service;


import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;
import com.pangu.resource.domain.bo.SysOssConfigBo;
import com.pangu.resource.domain.vo.SysOssConfigVo;

import java.util.Collection;

/**
 * 对象存储配置Service接口
 *
 * @author chengliang4810
 * @author chengliang4810
 * @date 2021-08-13
 */
public interface ISysOssConfigService {

    /**
     * 初始化OSS配置
     */
    void init();

    /**
     * 查询单个
     */
    SysOssConfigVo queryById(Long ossConfigId);

    /**
     * 查询列表
     */
    TableDataInfo<SysOssConfigVo> queryPageList(SysOssConfigBo bo, PageQuery pageQuery);


    /**
     * 根据新增业务对象插入对象存储配置
     *
     * @param bo 对象存储配置新增业务对象
     * @return
     */
    Boolean insertByBo(SysOssConfigBo bo);

    /**
     * 根据编辑业务对象修改对象存储配置
     *
     * @param bo 对象存储配置编辑业务对象
     * @return
     */
    Boolean updateByBo(SysOssConfigBo bo);

    /**
     * 校验并删除数据
     *
     * @param ids     主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 启用停用状态
     */
    int updateOssConfigStatus(SysOssConfigBo bo);

}
