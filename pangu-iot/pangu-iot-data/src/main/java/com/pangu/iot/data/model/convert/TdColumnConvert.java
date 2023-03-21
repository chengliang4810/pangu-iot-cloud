package com.pangu.iot.data.model.convert;

import com.pangu.common.tdengine.model.TdColumn;
import com.pangu.data.api.model.TdColumnDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TdColumnConvert {

    TdColumn dtoToEntity(TdColumnDTO tdColumnDTO);

    List<TdColumn> dtoToEntity(List<TdColumnDTO> tdColumnDTOList);

}
