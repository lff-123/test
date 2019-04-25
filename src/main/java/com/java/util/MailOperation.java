package com.java.util;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.util.Date;
import java.util.Properties;

/**
 * @Author: fangfang
 * @Date: 2019/3/5 17:06
 */
public class MailOperation {
    /**
     * 发送邮件
     * @param user 发件人邮箱
     * @param password 授权码（注意不是邮箱登录密码）
     * @param host
     * @param from 发件人
     * @param to 接收者邮箱
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return success 发送成功 failure 发送失败
     * @throws Exception
     */
    public String sendMail(String user, String password, String host,
                           String from, String to, String subject, String content,String file)
            throws Exception {
        if (to != null){
            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.auth", "true");
            MailAuthenticator auth = new MailAuthenticator();
            MailAuthenticator.USERNAME = user;
            MailAuthenticator.PASSWORD = password;
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                if (!to.trim().equals(""))
                    message.addRecipient(Message.RecipientType.TO,
                            new InternetAddress(to.trim()));
                message.setSubject(subject);
                MimeBodyPart mbp1 = new MimeBodyPart(); // 正文
                mbp1.setContent(content, "text/html;charset=utf-8");
                Multipart mp = new MimeMultipart(); // 整个邮件：正文+附件
                mp.addBodyPart(mbp1);

                if(null!=file){
                // 设置要发送附件的文件路径
                DataSource source = new FileDataSource("/var/www/html/documents/Email/"+file);
                mbp1.setDataHandler(new DataHandler(source));
                // 处理附件名称中文（附带文件路径）乱码问题
                mbp1.setFileName(MimeUtility.encodeText(file));
                }
                //mp.addBodyPart(mbp1);

                // mp.addBodyPart(mbp2);
                message.setContent(mp);
                message.setSentDate(new Date());
                message.saveChanges();
                Transport trans = session.getTransport("smtp");
                trans.send(message);
                System.out.println(message.toString());
            } catch (Exception e){
                e.printStackTrace();
                return "failure";
            }
            return "success";
        }else{
            return "failure";
        }
    }
}
