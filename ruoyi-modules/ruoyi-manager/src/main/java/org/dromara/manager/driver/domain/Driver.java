package org.dromara.manager.driver.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 驱动对象 iot_driver
 *
 * @author chengliang4810
 * @date 2023-06-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("iot_driver")
public class Driver extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 协议唯一性标识
     */
    private String code;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 启用|禁用
     */
    private Long enable;

    /**
     * 描述
     */
    private String remark;


}
