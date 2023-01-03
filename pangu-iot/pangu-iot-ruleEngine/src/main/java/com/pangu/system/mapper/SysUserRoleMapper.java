package com.pangu.system.mapper;

import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.system.domain.SysUserRole;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 *
 * @author chengliang4810
 */
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRoleMapper, SysUserRole, SysUserRole> {

    List<Long> selectUserIdsByRoleId(Long roleId);

}
