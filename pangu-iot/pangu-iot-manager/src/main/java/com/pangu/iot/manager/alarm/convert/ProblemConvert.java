package com.pangu.iot.manager.alarm.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.iot.manager.alarm.domain.Problem;
import com.pangu.iot.manager.alarm.domain.bo.ProblemBO;
import com.pangu.iot.manager.alarm.domain.vo.ProblemVO;
import com.pangu.manager.api.domain.dto.AlarmDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 告警记录Convert接口
 *
 * @author chengliang4810
 * @date 2023-02-13
 */
@Mapper(componentModel = "spring")
public interface ProblemConvert extends CommonConvert {


    /**
     * 实体
     *
     * @param alarmDTO 报警dto
     * @return {@link Problem}
     */
    Problem toEntity(AlarmDTO alarmDTO);

    /**
     * ProblemBO转换为ProblemEntity
     *
     * @param bo ProblemBO对象
     * @return problem
     */
    Problem toEntity(ProblemBO bo);


    /**
     * ProblemVO转换为ProblemEntity
     *
     * @param  vo ProblemVO对象
     * @return problem
     */
    Problem toEntity(ProblemVO vo);

    /**
     * Problem转换为ProblemBO
     *
     * @param  entity Problem对象
     * @return problemBO
     */
    ProblemBO toBo(Problem entity);

    /**
     * Problem转换为ProblemVO
     *
     * @param entity Problem
     * @return problemVO
     */
    ProblemVO toVo(Problem entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ProblemBO>对象
     * @return List<Problem>
     */
    List<Problem> boListToEntityList(List<ProblemBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ProblemVO>
     * @return List<Problem>
     */
    List<Problem> voListToEntityList(List<ProblemVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<Problem>
     * @return List<ProblemBO>
     */
    List<ProblemBO> toBoList(List<Problem> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<Problem>
     * @return List<ProblemVO>
     */
    List<ProblemVO> toVoList(List<Problem> entities);

}
