package com.pangu.iot.data.dubbo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.pangu.common.core.constant.IotConstants;
import com.pangu.common.tdengine.model.TdColumn;
import com.pangu.data.api.RemoteTdEngineService;
import com.pangu.data.api.model.TdColumnDTO;
import com.pangu.iot.data.model.convert.TdColumnConvert;
import com.pangu.iot.data.tdengine.service.TdEngineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.pangu.common.core.constant.IotConstants.TAG_OCCUPY_KEY;

/**
 * 操作TdEngine服务
 *
 * @author chengliang4810
 */
@Slf4j
@Service
@DubboService
@RequiredArgsConstructor
public class RemoteTdEngineServiceImpl implements RemoteTdEngineService {

    private final TdEngineService tdEngineService;

    private final TdColumnConvert tdColumnConvert;

    /**
     * 创建超级表
     */
    @Override
    public void createSuperTable(String table, List<TdColumnDTO> columns, List<TdColumnDTO> tags) {
        List<TdColumn> tdColumns = tdColumnConvert.dtoToEntity(columns);
        List<TdColumn> tdTags = tdColumnConvert.dtoToEntity(tags);
        tdEngineService.createSuperTable(table, tdColumns, tdTags);
    }

    /**
     * 初始化超级表
     * 按照业务调整字段信息
     * @param table
     */
    @Override
    public void initSuperTable(String table) {
        // 创建超级表默认字段
        List<TdColumn> tdColumns = ListUtil.of(new TdColumn("ts", "TIMESTAMP"), new TdColumn("online", "int"));
        List<TdColumn> tags = ListUtil.of(new TdColumn(TAG_OCCUPY_KEY, "int"));
        tdEngineService.createSuperTable(table, tdColumns, tags);
    }

    /**
     * 删除超级表
     *
     * @param table 表格
     */
    @Override
    public void deleteSuperTable(String table) {
        tdEngineService.deleteSuperTable(table);
    }

    /**
     * 创建表字段
     *
     * @param table        table
     * @param key       关键
     * @param valueType 值类型
     */
    @Override
    public void createSuperTableField(String table, String key, String valueType) {
        tdEngineService.createSuperTableField(table, key, valueType);
    }

    /**
     * 删除超级表字段
     *
     * @param table 表格
     * @param key   关键
     */
    @Override
    public void deleteSuperTableField(String table, String key) {
        tdEngineService.deleteSuperTableField(table, key);
    }

    /**
     * 今天最后一行数据
     *
     * @param productId  产品id
     * @param deviceCode 设备代码
     * @return {@link Map}<{@link String}, {@link Object}>
     */
    @Override
    public Map<String, Object> todayLastRowData(Long productId, String deviceCode) {
        try {
            return tdEngineService.selectLastData(StrUtil.format(IotConstants.DEVICE_TABLE_NAME_TEMPLATE, productId, deviceCode));
        } catch (Exception e) {
            log.warn("查询今天最后一行数据失败", e);
        }
        return Collections.emptyMap();
    }

}
