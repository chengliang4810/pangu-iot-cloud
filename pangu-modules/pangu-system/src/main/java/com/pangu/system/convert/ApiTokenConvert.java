package com.pangu.system.convert;

import com.pangu.common.core.convert.CommonConvert;
import com.pangu.system.api.model.ApiTokenDTO;
import com.pangu.system.domain.ApiToken;
import com.pangu.system.domain.bo.ApiTokenBO;
import com.pangu.system.domain.vo.ApiTokenVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 三方授权Convert接口
 *
 * @author chengliang4810
 * @date 2023-03-14
 */
@Mapper(componentModel = "spring")
public interface ApiTokenConvert extends CommonConvert {

    /**
     * ApiTokenBO转换为ApiTokenEntity
     *
     * @param bo ApiTokenBO对象
     * @return apiToken
     */
    ApiToken toEntity(ApiTokenBO bo);


    /**
     * ApiTokenVO转换为ApiTokenEntity
     *
     * @param  vo ApiTokenVO对象
     * @return apiToken
     */
    ApiToken toEntity(ApiTokenVO vo);

    /**
     * ApiToken转换为ApiTokenBO
     *
     * @param  entity ApiToken对象
     * @return apiTokenBO
     */
    ApiTokenBO toBo(ApiToken entity);

    /**
     * ApiToken转换为ApiTokenVO
     *
     * @param entity ApiToken
     * @return apiTokenVO
     */
    ApiTokenVO toVo(ApiToken entity);

    /**
     * BO List 转换为 Entity List
     *
     * @param bos List<ApiTokenBO>对象
     * @return List<ApiToken>
     */
    List<ApiToken> boListToEntityList(List<ApiTokenBO> bos);

    /**
     * VO List 转换为 Entity List
     *
     * @param vos List<ApiTokenVO>
     * @return List<ApiToken>
     */
    List<ApiToken> voListToEntityList(List<ApiTokenVO> vos);

    /**
     * Entity List 转换为 BO List
     *
     * @param entities List<ApiToken>
     * @return List<ApiTokenBO>
     */
    List<ApiTokenBO> toBoList(List<ApiToken> entities);

    /**
     * Entity List 转换为 VO List
     *
     * @param entities List<ApiToken>
     * @return List<ApiTokenVO>
     */
    List<ApiTokenVO> toVoList(List<ApiToken> entities);

    ApiTokenDTO toDTO(ApiToken apiToken);

    List<ApiTokenDTO> toDtoList(List<ApiToken> apiTokenList);
}
