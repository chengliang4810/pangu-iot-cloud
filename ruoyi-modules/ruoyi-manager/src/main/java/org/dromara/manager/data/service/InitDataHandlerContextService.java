package org.dromara.manager.data.service;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.manager.data.context.DataHandlerContext;
import org.dromara.manager.data.dto.DeviceAttributeValue;
import org.dromara.manager.device.domain.vo.DeviceAttributeVo;
import org.dromara.manager.device.service.IDeviceAttributeService;

/**
 * 初始化数据处理程序上下文服务
 *
 * @author chengliang
 * @date 2023/07/14
 */
@Slf4j
@RequiredArgsConstructor
@LiteflowComponent(value = "init", name = "初始化上下文节点")
public class InitDataHandlerContextService extends NodeComponent {

    private final IDeviceAttributeService deviceAttributeService;

    /**
     * 入参检查，是否可以执行
     *
     * @return boolean
     */
    @Override
    public boolean isAccess() {
        DeviceAttributeValue deviceAttributeValue = this.getRequestData();
        return StrUtil.isNotBlank(deviceAttributeValue.getDeviceCode())
            && StrUtil.isNotBlank(deviceAttributeValue.getValue())
            && StrUtil.isNotBlank(deviceAttributeValue.getDeviceCode());
    }

    @Override
    public void process() throws Exception {
        DataHandlerContext context = this.getContextBean(DataHandlerContext.class);
        DeviceAttributeValue deviceAttributeValue = this.getRequestData();

        // 查询设备属性列表 TODO: 2023/07/14 这里可以优化，该方法加入缓存
        DeviceAttributeVo deviceAttribute = deviceAttributeService.queryByCodeAndIdentifier(deviceAttributeValue.getDeviceCode(), deviceAttributeValue.getIdentifier());
        if (ObjUtil.isNull(deviceAttribute)) {
            return;
        }

        // 设置上下文
        context.setOriginalValue(deviceAttributeValue.getValue());
        context.setValue(deviceAttributeValue.getValue());
        context.setIdentifier(deviceAttributeValue.getIdentifier());
        context.setAttributeType(deviceAttribute.getAttributeType());
        context.setDeviceCode(deviceAttributeValue.getDeviceCode());
        context.setDeviceId(deviceAttribute.getDeviceId());
        context.setAttributeScript(deviceAttribute.getPretreatmentScript());
        log.info("初始化数据处理程序上下文服务完成，context:{}", context);
    }

    /**
     * 是否结束流程
     *
     * @return boolean
     */
    @Override
    public boolean isEnd() {
        DataHandlerContext context = this.getContextBean(DataHandlerContext.class);
        return context.getDeviceId() == null;
    }
}
