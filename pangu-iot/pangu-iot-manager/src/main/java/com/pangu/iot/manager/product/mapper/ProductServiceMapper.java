package com.pangu.iot.manager.product.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pangu.common.mybatis.core.mapper.BaseMapperPlus;
import com.pangu.iot.manager.product.domain.ProductService;
import com.pangu.iot.manager.product.domain.vo.ProductServiceVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * 产品功能Mapper接口
 *
 * @author chengliang4810
 * @date 2023-02-06
 */
public interface ProductServiceMapper extends BaseMapperPlus<ProductServiceMapper, ProductService, ProductServiceVO> {


    @Select("select service.*, inherit,relation_id  from iot_product_service service inner join iot_product_service_relation relation on service.id = relation.service_id ${ew.customSqlSegment}")
    List<ProductServiceVO> selectProduceVoList(@Param(Constants.WRAPPER) Wrapper<ProductService> queryWrapper);


    @Select("select service.*, inherit,relation_id from iot_product_service service inner join iot_product_service_relation relation on service.id = relation.service_id ${ew.customSqlSegment}")
    Page<ProductServiceVO> selectProduceVoPageList(IPage<ProductService> page, @Param(Constants.WRAPPER) Wrapper<ProductService> queryWrapper);

}
