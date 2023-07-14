package org.dromara.manager.data.service;

import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.script.ScriptExecutor;
import org.dromara.manager.data.context.DataHandlerContext;

import static org.dromara.manager.data.constant.ScriptConstants.ATTRIBUTE_VALUE_SCRIPT_TPL;

@Slf4j
@LiteflowComponent(value = "script", name = "属性脚本节点")
public class AttributeScriptService extends NodeComponent {


    /**
     * 未设置脚本则跳过该流程
     *
     * @return boolean
     */
//    @Override
//    public boolean isAccess() {
//        return StrUtil.isNotBlank(this.getContextBean(DataHandlerContext.class).getAttributeScript());
//    }

    @Override
    public void process() throws Exception {
        DataHandlerContext context = this.getContextBean(DataHandlerContext.class);
        // 执行脚本
        String script = context.getAttributeScript();
        String value = context.getValue();
        String result = this.executeScript(script, value);
        context.setValue(result);
        log.debug("属性脚本节点结束，原始值：{}，转换后：{}", value, result);
    }

    private String executeScript(String script, String value) {
        script = "return value;";
        return ScriptExecutor.execute(ATTRIBUTE_VALUE_SCRIPT_TPL, script, value);
    }

}
