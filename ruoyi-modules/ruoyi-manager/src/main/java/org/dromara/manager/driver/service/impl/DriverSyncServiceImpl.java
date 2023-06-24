package org.dromara.manager.driver.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.dromara.common.emqx.utils.EmqxUtil;
import org.dromara.common.iot.constant.DriverTopic;
import org.dromara.common.iot.dto.DriverSyncDownDTO;
import org.dromara.common.iot.dto.DriverSyncUpDTO;
import org.dromara.common.iot.entity.driver.DriverMetadata;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.manager.driver.service.DriverSyncService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DriverSyncServiceImpl implements DriverSyncService {


    /**
     * 驱动注册上线
     *
     * @param entityDTO 实体dto
     */
    @Override
    public void up(DriverSyncUpDTO entityDTO) {

//        if (ObjectUtil.isNull(entityDTO) || ObjectUtil.isNull(entityDTO.getDriver())) {
//            return;
//        }

        DriverMetadata driverMetadata = new DriverMetadata();

        DriverSyncDownDTO driverSyncDownDTO = new DriverSyncDownDTO(JsonUtils.toJsonString(driverMetadata));

        EmqxUtil.getClient().publish(DriverTopic.getDriverRegisterBackTopic(entityDTO.getDriverUniqueKey()), JsonUtils.toJsonString(driverSyncDownDTO));
        System.out.println("驱动注册发送回调指令。。。。" + entityDTO.getDriverUniqueKey());
    }



}
