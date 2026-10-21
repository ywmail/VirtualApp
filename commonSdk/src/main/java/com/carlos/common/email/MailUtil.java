/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.sun.mail.util.MailSSLSocketFactory
 *  javax.mail.Address
 *  javax.mail.Authenticator
 *  javax.mail.Message
 *  javax.mail.Message$RecipientType
 *  javax.mail.MessagingException
 *  javax.mail.PasswordAuthentication
 *  javax.mail.Session
 *  javax.mail.Transport
 *  javax.mail.internet.InternetAddress
 *  javax.mail.internet.MimeMessage
 */
package com.carlos.common.email;

import com.kook.librelease.StringFog;
import com.sun.mail.util.MailSSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {
    private String senderAccount;
    private String senderPassword;

    public MailUtil(String senderAccount, String senderPassword) {
        this.senderAccount = senderAccount;
        this.senderPassword = senderPassword;
    }

    public void send(String senderObject, String title, String content) throws MessagingException, GeneralSecurityException {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.qq.com");
        properties.put("mail.smtp.auth", "true");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getDefaultInstance((Properties)properties, (Authenticator)new Authenticator(){

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(MailUtil.this.senderAccount, MailUtil.this.senderPassword);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom((Address)new InternetAddress(this.senderAccount));
        message.addRecipient(Message.RecipientType.TO, (Address)new InternetAddress(senderObject));
        message.setSubject(title);
        message.setText(content);
        Transport.send((Message)message);
    }
}

