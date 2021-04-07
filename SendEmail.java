package com.company;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail
{
    private String email;
    private String emailMessage;

    public SendEmail(String email, String emailMessage){
        this.emailMessage = emailMessage;
        this.email = email;
        String from = "************@********.com";
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.imap.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.transport.protocol", "smtp");
        final String username = "************@********.com";
        final String password = "*********";
        try{
            Session session = Session.getDefaultInstance(properties,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication(){
                            return new PasswordAuthentication(username, password);
                        }
                    });
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, false));
            message.setSubject("MESSAGE FROM NC BANK!!");
            message.setText(emailMessage);

            Transport.send(message);
            System.out.println("MESSAGE SENT SUCCESSFULLY!!");

        }catch (MessagingException mex) {mex.printStackTrace();}
    }
}