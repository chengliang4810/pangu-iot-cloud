package com.pangu.demo.domain.bo;

import com.pangu.common.core.validate.AddGroup;
import com.pangu.common.core.validate.EditGroup;
import com.pangu.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 测试单表业务对象 test_demo
 *
 * @author chengliang4810
 * @date 2021-07-26
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class TestDemoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long deptId;

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long userId;

    /**
     * 排序号
     */
    @NotNull(message = "排序号不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer orderNum;

    /**
     * key键
     */
    @NotBlank(message = "key键不能为空", groups = {AddGroup.class, EditGroup.class})
    private String testKey;

    /**
     * 值
     */
    @NotBlank(message = "值不能为空", groups = {AddGroup.class, EditGroup.class})
    private String value;

}
