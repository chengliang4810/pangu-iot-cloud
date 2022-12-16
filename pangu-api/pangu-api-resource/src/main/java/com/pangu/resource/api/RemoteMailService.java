package com.pangu.resource.api;

import com.pangu.common.core.exception.ServiceException;

import java.io.File;
import java.util.List;

/**
 * 邮件服务
 *
 * @author chengliang4810
 */
public interface RemoteMailService {

    /**
     * 发送邮件
     *
     * @param to      接收人
     * @param subject 标题
     * @param text    内容
     */
    void send(String to, String subject, String text) throws ServiceException;

    /**
     * 发送邮件带附件
     *
     * @param to       接收人
     * @param subject  标题
     * @param text     内容
     * @param fileList 附件
     */
    void sendWithAttachment(String to, String subject, String text, List<File> fileList) throws ServiceException;

}
