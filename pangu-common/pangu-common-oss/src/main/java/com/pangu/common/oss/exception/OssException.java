package com.pangu.common.oss.exception;

/**
 * OSS异常类
 *
 * @author chengliang4810
 */
public class OssException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OssException(String msg) {
        super(msg);
    }

}
