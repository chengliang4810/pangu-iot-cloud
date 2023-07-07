package org.dromara.manager.driver.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.manager.driver.domain.PointAttributeValue;
import org.dromara.manager.driver.domain.vo.PointAttributeValueVo;

import java.util.List;

/**
 * 驱动属性值Mapper接口
 *
 * @author chengliang4810
 * @date 2023-06-19
 */
public interface PointAttributeValueMapper extends BaseMapperPlus<PointAttributeValue, PointAttributeValueVo> {

    /**
     * 查询
     * 手动拼接租户条件
     * @param deviceId          设备id
     * @param deviceAttributeId 设备属性id
     * @param driverId          驱动Id
     * @param tenantId        租户Id
     * @return {@link List}<{@link PointAttributeValueVo}>
     */
    @InterceptorIgnore(tenantLine = "true")
    List<PointAttributeValueVo> selectBy(@Param("deviceId") Long deviceId, @Param("deviceAttributeId") Long deviceAttributeId, @Param("driverId") Long driverId,@Param("tenantId") String tenantId);

}
