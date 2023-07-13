package org.dromara.data.api;

/**
 * 数据表服务
 *
 * @author chengliang4810
 */
public interface RemoteTableService {

    /**
     * 初始化超级表
     *
     * @param productId 产品id
     */
    void initSuperTable(Long productId);

    /**
     * 删除超级表
     *
     * @param productId 产品id
     */
    void dropSuperTable(Long productId);

    /**
     * 添加超级表字段
     *
     * @param productId     产品id
     * @param identifier    标识符
     * @param attributeType 属性类型
     */
    void addSuperTableField(Long productId, String identifier, String attributeType);

    /**
     * 删除超级表字段
     *
     * @param productId 产品id
     * @param identifier 标识符
     */
    void deleteSuperTableField(Long productId, String identifier);

    /**
     * 创建表
     *
     * @param productId 产品id
     * @param deviceCode  设备编号
     */
    void createTable(Long productId, String deviceCode);
}
