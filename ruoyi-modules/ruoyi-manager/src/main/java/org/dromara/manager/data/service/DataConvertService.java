package org.dromara.manager.data.service;

import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.dromara.manager.data.context.DataHandlerContext;
import org.dromara.manager.data.dto.DeviceAttributeValue;
import org.dromara.manager.data.utils.ConvertUtil;

/**
 * 数据转换服务
 *
 * @author chengliang
 * @date 2023/07/14
 */
@Slf4j
@LiteflowComponent(value = "convert", name = "数据转换节点")
public class DataConvertService extends NodeComponent {

    @Override
    public boolean isAccess() {
        DataHandlerContext context = this.getContextBean(DataHandlerContext.class);
        return StrUtil.isNotBlank(context.getAttributeType());
    }

    @Override
    public void process() throws Exception {
        DataHandlerContext context = this.getContextBean(DataHandlerContext.class);
        DeviceAttributeValue deviceValue = this.getRequestData();
        // 按照属性类型转换上传的数据，让数据尽可能的规范
        String newValue = ConvertUtil.convertValue(context.getAttributeType(), deviceValue.getValue());
        context.setValue(newValue);
        log.debug("数据转换节点结束，原始值：{}，转换后：{}", deviceValue.getValue(), newValue);
    }

}
