package com.java.util;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @Author: fangfang
 * @Date: 2019/3/5 17:04
 */
public class MailAuthenticator extends Authenticator {
    public static String USERNAME = "ljxh_123@163.com";
    public static String PASSWORD = "shljxhdsqm123";

    public MailAuthenticator() {
    }


    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(USERNAME, PASSWORD);
    }
}
