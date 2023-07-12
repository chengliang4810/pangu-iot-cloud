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
}
