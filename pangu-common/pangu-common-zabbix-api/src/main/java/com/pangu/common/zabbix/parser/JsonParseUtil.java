package com.pangu.common.zabbix.parser;

import freemarker.cache.SoftCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.StringWriter;
import java.util.Map;

/**
 * json解析
 *
 * @author chengliang
 * @date 2023/03/22
 */
public class JsonParseUtil {

    private static final Configuration configuration;

    static {
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(JsonParseUtil.class, "/api-json");
        configuration.setCacheStorage(new SoftCacheStorage());
        configuration.setTemplateUpdateDelayMilliseconds(1000);
        try {
            configuration.setSetting("number_format", "computer");
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Freemarker 解析 JSON API 请求数据
     *
     * @param jsonPath json 文件路径
     * @param args     入参
     * @return 返回 JSON 串
     */
    public static String parse(String jsonPath, Map<String, Object> args) {
        try {
            Template template = configuration.getTemplate(jsonPath);

            StringWriter out = new StringWriter();
            template.process(args, out);

            return out.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
