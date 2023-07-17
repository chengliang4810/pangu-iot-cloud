package org.dromara.data.service.impl;

import jakarta.annotation.Resource;
import org.dromara.common.iot.dto.StoreValueDTO;
import org.dromara.data.constant.TableConstants;
import org.dromara.data.service.DataService;
import org.dromara.data.service.TdEngineService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DataServiceImpl implements DataService {

    @Resource
    private TdEngineService tdEngineService;

    @Override
    public void storageTdEngine(StoreValueDTO storeValue) {
        Long ts = storeValue.getOriginTime();
        Map<String, Object> data = new HashMap<>(2);
        data.put(TableConstants.TABLE_PRIMARY_FIELD, ts);
        data.put(storeValue.getIdentifier(), storeValue.getValue());
        tdEngineService.insertData(storeValue.getDeviceCode(), data);
    }

}
