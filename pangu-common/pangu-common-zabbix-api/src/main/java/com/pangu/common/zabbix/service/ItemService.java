package com.pangu.common.zabbix.service;

import cn.hutool.json.JSONUtil;
import com.pangu.common.core.utils.JsonUtils;
import com.pangu.common.zabbix.api.ZbxItem;
import com.pangu.common.zabbix.entity.dto.TrapperItemDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 监控项服务
 *
 * @author chengliang
 * @date 2022/11/02
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ZbxItem zbxItem;

    /**
     * 创建监控项
     *
     * @param trapperItemDTO 创建监控项dto
     * @return {@link String}
     */
    public String createTrapperItem(TrapperItemDTO trapperItemDTO) {
        log.debug("创建监控项:{}", JSONUtil.toJsonStr(trapperItemDTO));
        System.out.println("创建监控项:" + JSONUtil.toJsonStr(trapperItemDTO));
        String response = zbxItem.createTrapperItem(trapperItemDTO.getItemName(), trapperItemDTO.getKey(), trapperItemDTO.getHostId(), trapperItemDTO.getSource(), "2s", trapperItemDTO.getDependencyItemZbxId(), trapperItemDTO.getValueType(), trapperItemDTO.getUnits(), null, trapperItemDTO.getValueMapId(), trapperItemDTO.getTags(), null);
        return JsonUtils.parseObject(response, TemplateIds.class).getItemids()[0];
    }

    /**
     * 更新监控项
     *
     * @param trapperItemDTO 更新监控项dto
     * @return {@link String}
     */
    public String updateTrapperItem(TrapperItemDTO trapperItemDTO) {
        log.debug("更新监控项:{}", JsonUtils.toJsonString(trapperItemDTO));
        String response = zbxItem.updateTrapperItem(trapperItemDTO.getItemId()
                , trapperItemDTO.getItemName()
                , trapperItemDTO.getKey()
                , trapperItemDTO.getHostId()
                , trapperItemDTO.getSource()
                , "2s"
                , trapperItemDTO.getDependencyItemZbxId()
                , trapperItemDTO.getValueType()
                , trapperItemDTO.getUnits()
                , null,
                trapperItemDTO.getValueMapId()
                , null
                , null);
        return JsonUtils.parseObject(response, TemplateIds.class).getItemids()[0];
    }

    /**
     * 删除监控项
     *
     * @param itemIds zbx id
     * @return {@link String}
     */
    public String deleteTrapperItems(List<String> itemIds) {
        return zbxItem.deleteTrapperItem(itemIds);
    }

    /**
     * 删除监控项
     *
     * @param itemId zbx id
     * @return {@link String}
     */
    public String deleteTrapperItem(String itemId) {
        return deleteTrapperItems(Collections.singletonList(itemId));
    }

        @Data
        static class TemplateIds {
            private String[] itemids;
        }

    }
