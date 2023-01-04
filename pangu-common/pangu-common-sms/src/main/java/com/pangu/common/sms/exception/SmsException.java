package com.pangu.common.sms.exception;

/**
 * Sms异常类
 *
 * @author chengliang4810
 */
public class SmsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SmsException(String msg) {
        super(msg);
    }

}
