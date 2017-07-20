package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

public class EmailHelper {
    private static final Logger LOG = LoggerFactory.getLogger(EmailHelper.class);

    private Properties properties;
    private String senderEmail;
    private String senderPassword;
    private String adminEmail;

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

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
        Session session = Session.getInstance(properties, new Authenticator(){
            @Override
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
        message.setSubject(subject);
        message.setContent(text, "text/html; charset=utf-8");

        Transport.send(message);
    }

    public Map.Entry<String, String> readTemplateFile(File file) {
        Map.Entry<String, String> result = null;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String templateName = reader.readLine();
            StringBuilder templateBody = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                templateBody.append(str);
                templateBody.append(System.lineSeparator());
            }
            result = new AbstractMap.SimpleEntry<>(templateName, templateBody.toString());
        } catch (IOException e) {
            LOG.warn("can't read template file - {}", file.getName(), e);
        }
        return result;
    }

    public Map.Entry<String, String> readTemplateFile(String fileName) {
        try {
            return readTemplateFile(new File(getClass().getResource("../" + fileName).toURI()));
        } catch (URISyntaxException e) {
            LOG.error("error while opening file {}", fileName, e);
        }
        return null;
    }

    public String processTemplate(String templateText, Map<String, Object> args) {
        ST template = new ST(templateText);

        for (String key : args.keySet()) {
            template.add(key, args.get(key));
        }

        return template.render();
    }

}
