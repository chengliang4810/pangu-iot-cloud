package org.dromara.manager.driver.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.manager.driver.domain.bo.DriverApplicationBo;
import org.dromara.manager.driver.domain.vo.DriverApplicationVo;

import java.util.Collection;
import java.util.List;

/**
 * 驱动应用Service接口
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
public interface IDriverApplicationService {

    /**
     * 查询驱动应用
     */
    DriverApplicationVo queryById(Long id);

    /**
     * 查询驱动应用列表
     */
    TableDataInfo<DriverApplicationVo> queryPageList(DriverApplicationBo bo, PageQuery pageQuery);

    /**
     * 查询驱动应用列表
     */
    List<DriverApplicationVo> queryList(DriverApplicationBo bo);

    /**
     * 新增驱动应用
     */
    Boolean insertByBo(DriverApplicationBo bo);

    /**
     * 修改驱动应用
     */
    Boolean updateByBo(DriverApplicationBo bo);

    /**
     * 校验并批量删除驱动应用信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
