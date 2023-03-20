package com.pangu.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 三方授权对象 sys_api_token
 *
 * @author chengliang4810
 * @date 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_api_token")
public class ApiToken extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * token
     */
    private String token;
    /**
     * 过期时间
     */
    private Date expirationTime;
    /**
     * 状态（0正常 1停用）
     */
    private Boolean status;
    /**
     * 备注
     */
    private String remark;

}
