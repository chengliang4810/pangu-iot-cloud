package com.pangu.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 角色和部门关联 sys_role_dept
 *
 * @author chengliang4810
 */

@Data
@NoArgsConstructor
@TableName("sys_role_dept")
public class SysRoleDept {

    /**
     * 角色ID
     */
    @TableId(type = IdType.INPUT)
    private Long roleId;

    /**
     * 部门ID
     */
    private Long deptId;

}
