package util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.ResourceBundle;

public class EmailHelper {
    private Properties properties;
    private String senderEmail;
    private String senderPassword;
    private String adminEmail;
    public EmailHelper() {
        properties = new Properties();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("email");
        properties.put("mail.smtp.host", resourceBundle.getString("mail.smtp.host"));
        properties.put("mail.smtp.port", resourceBundle.getString("mail.smtp.port"));
        properties.put("mail.smtp.auth", resourceBundle.getString("mail.smtp.auth"));
        properties.put("mail.smtp.ssl.trust", resourceBundle.getString("mail.smtp.ssl.trust"));
        properties.put("mail.smtp.starttls.enable",resourceBundle.getString("mail.smtp.starttls.enable"));
        properties.put("mail.debug", resourceBundle.getString("mail.debug"));

        senderEmail = resourceBundle.getString("sender.email");
        senderPassword = resourceBundle.getString("sender.password");
        adminEmail = resourceBundle.getString("admin.email");
    }

    public void sendEmail(String receiverEmail, String subject, String text) throws MessagingException {
        Session session = Session.getDefaultInstance(properties, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
        message.setSubject(subject);
        message.setText(text);

        Transport.send(message);
    }

    public void sendToAdmin(String subject, String text) throws MessagingException {
        sendEmail(adminEmail, subject, text);
    }
}
