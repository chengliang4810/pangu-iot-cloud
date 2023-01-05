package com.pangu.common.zabbix.inteceptor;

import com.dtflys.forest.config.ForestConfiguration;
import com.dtflys.forest.http.ForestRequest;
import com.dtflys.forest.http.ForestResponse;
import com.dtflys.forest.interceptor.Interceptor;
import com.dtflys.forest.reflection.ForestMethod;
import com.dtflys.forest.utils.StringUtils;
import com.google.gson.Gson;
import com.pangu.common.zabbix.annotation.JsonPath;
import com.pangu.common.zabbix.annotation.ParamName;
import com.pangu.common.zabbix.entity.ZbxResponseData;
import com.pangu.common.zabbix.parser.JsonParseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author chengliang
 * @version 1.0  拦截器 基于 JSON 文件构建 JSON String 参数
 * @date 2022/11/01
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JsonBodyBuildInterceptor implements Interceptor<String> {

    private static final String NO_AUTH_TAG = "authTag";

    private static final Gson gson = new Gson();

    private final ForestConfiguration forestConfiguration;
    /**
     * 方法调用之前获取 JSON path
     */
    @Override
    public void onInvokeMethod(ForestRequest request, ForestMethod method, Object[] args) {
        Map<String, Object> paramMap = new HashMap<>();

        if (request.getHeader(NO_AUTH_TAG) == null || !"noAuth".equals(request.getHeader(NO_AUTH_TAG).getValue())) {
            paramMap.put("userAuth", forestConfiguration.getVariableValue("zbxApiToken"));
        }

        if (null != args && args.length > 0) {
            // 优先 Map 的处理
            if (args[0] instanceof Map) {
                paramMap.putAll((Map) args[0]);
            } else {
                Annotation[][] paramAnnos = method.getMethod().getParameterAnnotations();
                for (int i = 0; i < args.length; i++) {
                    if (paramAnnos[i].length > 0 && paramAnnos[i][0] instanceof ParamName) {
                        ParamName paramAnno = (ParamName) paramAnnos[i][0];
                        paramMap.put(paramAnno.value(), args[i]);
                    }
                }
            }
        }

        JsonPath jsonPath = method.getMethod().getAnnotation(JsonPath.class);
        if (null != jsonPath && StringUtils.isNotBlank(jsonPath.value())) {
            String body = Objects.requireNonNull(JsonParseUtil.parse(jsonPath.value() + ".ftl", paramMap));
            String sendBody = StringEscapeUtils.unescapeJava(body);
            log.debug("\n" + sendBody + "\n");
            request.replaceBody(sendBody);
            request.setContentType("application/json");
        }
    }

    /**
     * Zabbix 接口异常返回异常信息捕捉
     */
    @Override
    public void onSuccess(String data, ForestRequest request, ForestResponse response) {

        ZbxResponseData responseData = gson.fromJson(data, ZbxResponseData.class);

        if (null != responseData.getError()) {
            log.error(response.getContent());
            throw new RuntimeException(responseData.getError().getData());
        }
        response.setResult(responseData.getResult());
        Interceptor.super.onSuccess(data, request, response);
    }
}
