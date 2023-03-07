package com.pangu.iot.manager.device.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pangu.common.core.utils.Assert;
import com.pangu.common.zabbix.service.DeviceStatusTriggerService;
import com.pangu.iot.manager.device.convert.DeviceStatusFunctionConvert;
import com.pangu.manager.api.domain.DeviceAttribute;
import com.pangu.iot.manager.device.domain.DeviceStatusFunction;
import com.pangu.iot.manager.device.domain.DeviceStatusFunctionRelation;
import com.pangu.iot.manager.device.domain.bo.DeviceStatusJudgeRuleBO;
import com.pangu.iot.manager.device.service.IDeviceAttributeService;
import com.pangu.iot.manager.device.service.IDeviceStatusFunctionRelationService;
import com.pangu.iot.manager.device.service.IDeviceStatusFunctionService;
import com.pangu.iot.manager.device.service.IDeviceTriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 设备触发器Service业务层处理
 *
 * @author chengliang4810
 * @date 2023-01-06
 */
@Service
@RequiredArgsConstructor
public class DeviceTriggerServiceImpl implements IDeviceTriggerService {


    private final IDeviceAttributeService deviceAttributeService;
    private final DeviceStatusTriggerService deviceStatusTriggerService;
    private final DeviceStatusFunctionConvert deviceStatusFunctionConvert;
    private final IDeviceStatusFunctionService deviceStatusFunctionService;
    private final IDeviceStatusFunctionRelationService deviceStatusFunctionRelationService;

    /**
     * 创建设备状态判断触发器
     *
     * @param judgeRule 规则
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createDeviceStatusJudgeTrigger(DeviceStatusJudgeRuleBO judgeRule) {

        // 参数检查
        long count = deviceAttributeService.count(Wrappers.lambdaQuery(DeviceAttribute.class).eq(DeviceAttribute::getId, judgeRule.getAttributeId()));
        Assert.isTrue(count > 0, "下线属性不存在");
        long recoveryCount = deviceAttributeService.count(Wrappers.lambdaQuery(DeviceAttribute.class).eq(DeviceAttribute::getId, judgeRule.getAttributeIdRecovery()));
        Assert.isTrue(recoveryCount > 0, "上线属性不存在");
        long relationCount = deviceStatusFunctionRelationService.count(Wrappers.lambdaQuery(DeviceStatusFunctionRelation.class).eq(DeviceStatusFunctionRelation::getRelationId, judgeRule.getRelationId()));
        Assert.isTrue(relationCount == 0, "该设备已经存在下线规则");

        // 生成唯一主Id
        Long ruleId = IdUtil.getSnowflakeNextId();
        judgeRule.setRuleId(ruleId);

        //step 1:保存到zbx 建立上线及下线规则
        String ruleCondition = judgeRule.getRuleCondition();
        if(!NumberUtil.isNumber(ruleCondition)){
            ruleCondition = "\""+ruleCondition+"\"";
        }

        String[] triggerIds = deviceStatusTriggerService.createDeviceStatusTrigger(judgeRule.getRuleId() + "", judgeRule.getRelationId().toString(),
                judgeRule.getAttributeKey(), ruleCondition + judgeRule.getUnit(), judgeRule.getRuleFunction(), judgeRule.getAttributeKeyRecovery(),
                judgeRule.getRuleConditionRecovery() + judgeRule.getUnitRecovery(), judgeRule.getRuleFunctionRecovery());

        //step 2:保存规则
        DeviceStatusFunction deviceStatusFunction = deviceStatusFunctionConvert.toEntity(judgeRule);
        deviceStatusFunction.setId(ruleId);
        deviceStatusFunctionService.save(deviceStatusFunction);

        //step 3:保存规则与产品的关联关系
        DeviceStatusFunctionRelation deviceStatusFunctionRelation = new DeviceStatusFunctionRelation();
        deviceStatusFunctionRelation.setRelationId(judgeRule.getRelationId());
        deviceStatusFunctionRelation.setRuleId(ruleId);
        //对应zbx下线规则ID
        deviceStatusFunctionRelation.setZbxId(triggerIds[0]);
        //对应zbx上线规则ID
        deviceStatusFunctionRelation.setZbxIdRecovery(triggerIds[1]);

        //step 4:同步到设备
        // Long productId = judgeRule.getRelationId();

        return deviceStatusFunctionRelationService.save(deviceStatusFunctionRelation);
    }

    /**
     * 更新设备状态判断触发器
     *
     * @param judgeRule  规则数据
     * @return int
     */
    @Override
    public Boolean updateDeviceStatusJudgeTrigger(DeviceStatusJudgeRuleBO judgeRule) {

        DeviceStatusFunctionRelation relation = deviceStatusFunctionRelationService.getOne(Wrappers.lambdaQuery(DeviceStatusFunctionRelation.class)
            .eq(DeviceStatusFunctionRelation::getRuleId, judgeRule.getRuleId()).eq(DeviceStatusFunctionRelation::getRelationId, judgeRule.getRelationId()).last("limit 1"));
        Assert.notNull(relation, "上下线规则不存在");

        String ruleCondition = judgeRule.getRuleCondition();
        if(!NumberUtil.isNumber(ruleCondition)){
            ruleCondition = "\""+ruleCondition+"\"";
        }

        // 更新zabbix
        deviceStatusTriggerService.updateDeviceStatusTrigger(relation.getZbxId(), judgeRule.getRuleId() + "", judgeRule.getRelationId().toString(),
            judgeRule.getAttributeKey(), ruleCondition + judgeRule.getUnit(), judgeRule.getRuleFunction(), judgeRule.getAttributeKeyRecovery(),
            judgeRule.getRuleConditionRecovery() + judgeRule.getUnitRecovery(), judgeRule.getRuleFunctionRecovery(), relation.getZbxIdRecovery());

        // 修改入库
        DeviceStatusFunction deviceStatusFunction = deviceStatusFunctionConvert.toEntity(judgeRule);
        deviceStatusFunction.setId(judgeRule.getRuleId());
        return deviceStatusFunctionService.updateById(deviceStatusFunction);
    }
}
