package io.xqs.bt.smspush.util;

import com.sun.mail.util.MailSSLSocketFactory;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import io.xqs.bt.smspush.model.ConfigMdl;


public class MailSender {

    private String mailContent;
    private String mailTitle;
    private ConfigMdl configMdl;

    public MailSender(String title, String content, ConfigMdl configMdl) {
        this.mailTitle = title;
        this.mailContent = content;
        this.configMdl = configMdl;
    }

    public void sendMail() throws MessagingException, GeneralSecurityException {
        Properties props = new Properties();
        props.put("mail.smtp.host", configMdl.getSmtp());
        props.setProperty("mail.host", configMdl.getSmtp());
        props.setProperty("mail.smtp.connectiontimeout","15000");
        //设置验证
        props.put("mail.smtp.auth", "true");
//        props.setProperty("mail.debug", "true");
        props.setProperty("mail.transport.protocol", "smtp");
//        props.put("mail.user", configMdl.getSmtpUser());
//        props.put("mail.password",  configMdl.getSmtpPass());
        if(configMdl.isUserSSL()){
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);
        }

//        MyAuthenticator myAuth = new MyAuthenticator(configMdl.getSmtpUser(), configMdl.getSmtpPass());
        Session session = Session.getInstance(props);
        //打开调试开关
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        InternetAddress fromAddress = null;
        //发件人邮箱地址
        fromAddress = new InternetAddress(configMdl.getSendMail());
        message.setFrom(fromAddress);
        InternetAddress toAddress = new InternetAddress(configMdl.getRecvMail());
        message.addRecipient(Message.RecipientType.TO, toAddress);
        message.setSubject(this.mailTitle);
        message.setContent(this.mailContent, "text/html; charset=utf-8");
        message.saveChanges(); //存储信息

        Transport transport = null;
        transport = session.getTransport("smtp");
        transport.connect(configMdl.getSmtp(), configMdl.getSmtpUser(), configMdl.getSmtpPass());
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

//    class MyAuthenticator extends javax.mail.Authenticator {
//        private String strUser;
//        private String strPwd;
//
//        public MyAuthenticator(String user, String password) {
//            this.strUser = user;
//            this.strPwd = password;
//        }
//
//        @Override
//        protected PasswordAuthentication getPasswordAuthentication() {
//            return new PasswordAuthentication(strUser, strPwd);
//        }
//    }
}