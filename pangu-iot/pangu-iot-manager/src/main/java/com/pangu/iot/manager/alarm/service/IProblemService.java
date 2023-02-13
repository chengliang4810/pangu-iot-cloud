package com.pangu.iot.manager.alarm.service;

import com.pangu.iot.manager.alarm.domain.Problem;
import com.pangu.iot.manager.alarm.domain.vo.ProblemVO;
import com.pangu.iot.manager.alarm.domain.bo.ProblemBO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pangu.common.mybatis.core.page.PageQuery;
import com.pangu.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 告警记录Service接口
 *
 * @author chengliang4810
 * @date 2023-02-13
 */
public interface IProblemService extends IService<Problem> {

    /**
     * 查询告警记录
     */
    ProblemVO queryById(Long eventId);

    /**
     * 查询告警记录列表
     */
    TableDataInfo<ProblemVO> queryPageList(ProblemBO bo, PageQuery pageQuery);

    /**
     * 查询告警记录列表
     */
    List<ProblemVO> queryList(ProblemBO bo);

    /**
     * 修改告警记录
     */
    Boolean insertByBo(ProblemBO bo);

    /**
     * 修改告警记录
     */
    Boolean updateByBo(ProblemBO bo);

    /**
     * 校验并批量删除告警记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 确认问题告警
     *
     * @param eventId 标识符
     * @return {@link Boolean}
     */
    Boolean acknowledgement(Long eventId);

    /**
     * 解决问题
     *
     * @param eventId 标识符
     * @return {@link Boolean}
     */
    Boolean resolve(Long eventId);
}
