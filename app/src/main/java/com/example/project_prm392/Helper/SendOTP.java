package com.example.project_prm392.Helper;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendOTP {
    public void sendMail(String receive_email, String email_subject, String text) {
        try {
            String senderEmail = "ghbntyuizs@gmail.com";
            String passwordSenderEmail = "omqo pieg dfyx xtxv";

            String stringHost = "smtp.gmail.com";
            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", 465);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, passwordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(receive_email));

            mimeMessage.setSubject(email_subject);
            mimeMessage.setText(text);

            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



}
