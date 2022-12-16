package com.pangu.demo.mapper;

import com.pangu.common.mybatis.annotation.DataColumn;
import com.pangu.common.mybatis.annotation.DataPermission;
import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.demo.domain.TestTree;
import com.pangu.demo.domain.vo.TestTreeVo;

/**
 * 测试树表Mapper接口
 *
 * @author chengliang4810
 * @date 2021-07-26
 */
@DataPermission({
    @DataColumn(key = "deptName", value = "dept_id"),
    @DataColumn(key = "userName", value = "user_id")
})
public interface TestTreeMapper extends BaseMapperPlus<TestTreeMapper, TestTree, TestTreeVo> {

}
