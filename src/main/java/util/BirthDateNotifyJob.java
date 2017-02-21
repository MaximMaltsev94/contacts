package util;

import dao.implementation.ConnectionFactory;
import exceptions.ConnectionException;
import exceptions.DaoException;
import model.Contact;
import model.User;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ContactService;
import service.ContactServiceImpl;
import service.UserService;
import service.UserServiceImpl;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BirthDateNotifyJob implements Job {
    private final static Logger LOG = LoggerFactory.getLogger(BirthDateNotifyJob.class);
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try(Connection connection = ConnectionFactory.getInstance().getConnection()) {
            LOG.info("start looking for birthday contacts");
            UserService userService = new UserServiceImpl(connection);
            ContactService contactService = new ContactServiceImpl(connection);

            List<User> userList = userService.getByNeedNotify(true);
            List<String> userLoginList = userList.stream().map(User::getLogin).collect(Collectors.toList());
            List<Contact> contactList = contactService.getByBirthdayAndLoginUserIn(new Date(), userLoginList);
            LOG.info("Contact who has birthday today: {}", contactList);
            Map<String, List<Contact>> contactMap = contactList.stream().collect(Collectors.groupingBy(Contact::getLoginUser));
            String startString = "Сегодня " + DateFormatUtils.format(new Date(), "dd.MM.yyyy") + " свой день рождения отмечают: ";
            EmailHelper emailHelper = new EmailHelper();
            for (User user : userList) {
                StringBuilder emailText = new StringBuilder();
                emailText.append(startString);
                for (Contact contact : contactMap.get(user.getLogin())) {
                    emailText.append(System.getProperty("line.separator"));
                    emailText.append(contact.getFirstName());
                    emailText.append(" ");
                    emailText.append(contact.getLastName());
                }
                emailHelper.sendEmail(user.getEmail(), "Уведомление о дне рождения", emailText.toString());
                LOG.info("birthday notify message sent to user with email - {}", user.getEmail());
            }
        } catch (SQLException e ) {
            LOG.error("can't close connection to database", e);
        } catch (MessagingException e) {
            LOG.error("can't send birthday notify email", e);
        } catch (ConnectionException e) {
            LOG.error("can't get connection to database", e);
        } catch (DaoException e) {
            LOG.error("error while accessing to database", e);
        }
    }
}
