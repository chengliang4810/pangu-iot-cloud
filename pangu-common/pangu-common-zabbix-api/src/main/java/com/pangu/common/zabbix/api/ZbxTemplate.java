package com.pangu.common.zabbix.api;

import com.dtflys.forest.annotation.Post;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;

import java.util.Map;

/**
 * zbx模板
 *
 * @author chengliang
 * @date 2023/03/22
 */
public interface ZbxTemplate extends BaseApi {

    /**
     * 创建模板
     *
     * @param templateName 模板名称
     * @param templateGroupId  模板组ID
     * @return String
     */
    @Post
    @JsonPath("/template/template.create")
    String templateCreate(@ParamName("templateName") String templateName,
                          @ParamName("groupId") String templateGroupId);


    /**
     * 删除模板
     *
     * @param templateid 模板ID
     * @return String
     */
    @Post
    @JsonPath("/template/template.delete")
    String templateDelete(@ParamName("templateid") String templateid);


    /**
     * 更新模板标签
     *
     * @param tagMap 标签Map
     * @return String
     */
    @Post
    @JsonPath("/template/template.tag.update")
    String templateTagUpdate(@ParamName("templateId") String templateId,
                             @ParamName("tagMap") Map<String, String> tagMap);

    /**
     * 查询模板详情
     *
     * @param templateid 模板ID
     */
    @Post
    @JsonPath("/template/template.detail.get")
    String templateDetail(@ParamName("templateid") String templateid);

    /**
     * 查询模板信息
     *
     * @param templateid 模板ID
     */
    @Post
    @JsonPath("/template/template.get")
    String templateGet(@ParamName("templateid") String templateid);
}
