package org.dromara.common.script;

import cn.hutool.core.util.StrUtil;
import cn.hutool.script.ScriptUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;

/**
 * 脚本执行器
 *
 * @author chengliang
 * @date 2023/07/14
 */
@Slf4j
public class ScriptExecutor {

    /**
     * 脚本引擎
     */
    private final static ScriptEngine SCRIPT_ENGINE = ScriptUtil.getJsEngine();

    private ScriptExecutor() {
        ScriptContext context = SCRIPT_ENGINE.getContext();
        JavaLoggingWriter loggingWriter = new JavaLoggingWriter();
        context.setWriter(loggingWriter);
        context.setErrorWriter(loggingWriter);
    }

    /**
     * 执行脚本
     *
     * @param script 脚本
     * @return {@link String}
     */
    public static String execute(String script, Object... params) {
        String formatResult = StrUtil.format(script, params);
        try {
            Object eval = SCRIPT_ENGINE.eval(formatResult);
            String result = eval == null ? "" : eval.toString();
            log.debug("执行脚本: {}, 返回值: {}", formatResult, result);
            return result;
        } catch (Exception e) {
            throw new ServiceException("脚本执行异常: {}", e.getMessage());
        }
    }

}
