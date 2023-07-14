package org.dromara.common.script;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Writer;

/**
 * java日志
 *
 * @author chengliang
 * @date 2023/07/14
 */
public class JavaLoggingWriter  extends Writer {

    private static final Logger logger = LoggerFactory.getLogger(ScriptExecutor.class);

    private StringBuilder buffer = new StringBuilder();

    @Override
    public void write(char[] cbuf, int off, int len) {
        buffer.append(cbuf, off, len);
    }

    @Override
    public void flush() {
        String output = buffer.toString().trim();
        if (!output.isEmpty()) {
            logger.info(output);
        }
        buffer = new StringBuilder();
    }

    @Override
    public void close() {
        flush();
    }

}
