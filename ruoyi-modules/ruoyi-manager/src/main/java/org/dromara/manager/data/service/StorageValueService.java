package org.dromara.manager.data.service;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.emqx.utils.EmqxUtil;
import org.dromara.common.iot.dto.StoreValueDTO;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.manager.data.context.DataHandlerContext;

@Slf4j
@RequiredArgsConstructor
@LiteflowComponent(value = "storage", name = "存储节点")
public class StorageValueService extends NodeComponent {

    @Override
    public boolean isAccess() {
        return StrUtil.isNotBlank(this.getContextBean(DataHandlerContext.class).getValue());
    }

    @Override
    public void process() throws Exception {
        DataHandlerContext context = this.getContextBean(DataHandlerContext.class);
        String deviceCode = context.getDeviceCode();
        String identifier = context.getIdentifier();
        Long time = context.getOriginTime();
        String value = context.getValue();

        // 转换DTO
        StoreValueDTO storeValue = new StoreValueDTO();
        storeValue.setDeviceCode(deviceCode);
        storeValue.setIdentifier(identifier);
        storeValue.setOriginTime(time);
        storeValue.setValue(value);

        // 通过emqx发送给data模块进行存储
        EmqxUtil.publish("", JsonUtils.toJsonString(storeValue));
    }


}
