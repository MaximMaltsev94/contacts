package util;

import dao.interfaces.ContactDao;
import dao.mysqlimplementation.MySqlConnectionFactory;
import dao.mysqlimplementation.MySqlContactDao;
import model.Contact;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by maxim on 04.10.2016.
 */
public class BirthDateNotifyJob implements Job {
    private final static Logger LOG = LoggerFactory.getLogger(BirthDateNotifyJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try(Connection connection = MySqlConnectionFactory.getInstance().getConnection()) {
            ContactDao contactDao = new MySqlContactDao(connection);
            List<Contact> contactList = contactDao.getByBirthdayToday();
            StringBuilder emailText = new StringBuilder();
            emailText.append("Сегодня свой день рождения отмечают: ");
            for (Contact contact : contactList) {
                emailText.append(System.getProperty("line.separator"));
                emailText.append(contact.getFirstName());
                emailText.append(" ");
                emailText.append(contact.getLastName());
            }
            EmailHelper emailHelper = new EmailHelper();
            emailHelper.sendToAdmin("Уведомление о дне рождения", emailText.toString());
        } catch (SQLException | NamingException e) {
            LOG.warn("can't get db connection", e);
        } catch (MessagingException e) {
            LOG.warn("can't send birthday notify email", e);
        }
    }
}
