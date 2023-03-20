package com.pangu.system.api.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApiTokenDTO implements Serializable {

    /**
     * 主键id
     */
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
