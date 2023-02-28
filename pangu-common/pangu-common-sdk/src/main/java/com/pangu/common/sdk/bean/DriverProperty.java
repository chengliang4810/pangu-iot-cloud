package com.pangu.common.sdk.bean;


import com.pangu.common.sdk.bean.schedule.ScheduleProperty;
import com.pangu.manager.api.domain.DriverAttribute;
import com.pangu.manager.api.domain.PointAttribute;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 驱动程序属性
 *
 * @author chengliang
 * @date 2023/02/28
 */
@Setter
@Getter
@Validated({Insert.class, Update.class})
@ConfigurationProperties(prefix = "driver")
public class DriverProperty {

    /**
     * 驱动代码
     */
    @NotBlank(message = "name can't be empty")
    @Pattern(
            regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/\\.\\|]{1,31}$",
            message = "Invalid name,contains invalid characters or length is not in the range of 2~32",
            groups = {Insert.class, Update.class})
    private String name;

    /**
     * 驱动名称
     */
    @NotBlank(message = "displayName can't be empty")
    @Length(min = 2, max = 32, groups = {Insert.class, Update.class}, message = "Invalid displayName,contains invalid characters or length is not in the range of 2~32")
    private String displayName;

    /**
     * 描述
     */
    private String description;
    /**
     * 轮询配置
     */
    private ScheduleProperty schedule;
    /**
     * 驱动程序属性
     */
    private List<DriverAttribute> driverAttribute;

    /**
     * 点位属性
     */
    private List<PointAttribute> pointAttribute;

}

