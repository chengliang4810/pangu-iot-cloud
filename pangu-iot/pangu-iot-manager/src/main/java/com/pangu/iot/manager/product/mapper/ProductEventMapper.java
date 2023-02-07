package com.pangu.iot.manager.product.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.iot.manager.product.domain.ProductEvent;
import com.pangu.iot.manager.product.domain.vo.ProductEventVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 告警规则Mapper接口
 *
 * @author chengliang4810
 * @date 2023-02-03
 */
public interface ProductEventMapper extends BaseMapperPlus<ProductEventMapper, ProductEvent, ProductEventVO> {

    /**
     * 查询volist
     *
     * @param lqw 查询条件
     * @return {@link ProductEventVO}
     */
    @Select("select event.*,relation_id,inherit from iot_product_event event inner join iot_product_event_relation event_relation on event.id = event_relation.event_rule_id ${ew.customSqlSegment}")
    Page<ProductEventVO> selectVoListPage(Page<ProductEvent> page, @Param("ew") Wrapper<ProductEvent> lqw);

}
