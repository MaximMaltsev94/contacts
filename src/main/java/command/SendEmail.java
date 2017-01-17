package command;

import dao.implementation.*;
import dao.interfaces.CityDao;
import dao.interfaces.ContactDao;
import dao.interfaces.CountryDao;
import dao.interfaces.RelationshipDao;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.Contact;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import util.ContactUtils;
import util.EmailHelper;
import util.RequestUtils;
import util.TooltipType;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;

public class SendEmail implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(SendEmail.class);

    private String processMessage(Contact contact, String relationship, String country, String city, String message) {
        ST template = new ST(message);
        template.add("firstName", contact.getFirstName());
        template.add("lastName", contact.getLastName());
        if(contact.getPatronymic() != null)
            template.add("patronymic", contact.getPatronymic());
        if(contact.getBirthDate() != null) {
            template.add("birthDate", DateFormatUtils.format(contact.getBirthDate(), "dd MMMM yyyy"));
        }
        String gender = "жен.";
        if(contact.getGender() == true) {
            gender = "муж.";
        }
        template.add("gender", gender);
        template.add("citizenship", contact.getCitizenship());
        template.add("relationship", relationship);
        template.add("website", contact.getWebSite());
        template.add("companyName", contact.getCompanyName());
        template.add("country", country);
        template.add("city", city);
        template.add("street", contact.getStreet());
        template.add("postcode", contact.getPostcode());
        return template.render();
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws CommandExecutionException, DataNotFoundException {

        Contact contact = null;
        boolean isErrorOccurred = true;
        try (Connection connection = ConnectionFactory.getInstance().getConnection()) {
            EmailHelper emailHelper = new EmailHelper();
            String emailSubject = ContactUtils.getUTF8String(request.getParameter("subject"));
            String emailText = ContactUtils.getUTF8String(request.getParameter("text"));
            String []splitedIds = request.getParameter("id").split(",");

            ContactDao contactDao = new ContactDaoImpl(connection);
            RelationshipDao relationshipDao = new RelationshipDaoImpl(connection);
            CountryDao countryDao = new CountryDaoImpl(connection);
            CityDao cityDao = new CityDaoImpl(connection);
            for (String id : splitedIds) {
                int contactId = Integer.parseInt(id);
                contact = contactDao.getByID(contactId);
                String relationship;
                try {
                    relationship = relationshipDao.getByID(contact.getRelationshipID()).getName();
                } catch (Exception e) {
                    relationship = "";
                }

                String country;
                try {
                    country = countryDao.getByID(contact.getCountryID()).getName();
                } catch (Exception e) {
                    country = "";
                }

                String city;
                try {
                    city = cityDao.getByID(contact.getCountryID()).getName();
                } catch (Exception e) {
                    city = "";
                }

                String message = processMessage(contact, relationship, country, city, emailText);

                emailHelper.sendEmail(contact.getEmail(), emailSubject, message);

            }
            isErrorOccurred = false;
        }  catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        } catch (ConnectionException e) {
            LOG.error("can't get connection to database", e);
            throw new CommandExecutionException("error while connecting to database", e);
        } catch (SQLException e) {
            LOG.error("can't close connection to database", e);
            throw new CommandExecutionException("error while closing database connection", e);
        } catch (MessagingException e) {
            LOG.error("can't send email to address - ", contact.getEmail(), e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("error while parsing encoding parameters", e);
        }

        if(isErrorOccurred) {
            RequestUtils.setMessageText(request, "Произошла ошибка при отправке письма", TooltipType.danger);
        } else {
            RequestUtils.setMessageText(request, "Письмо успешно отправлено", TooltipType.success);
        }

        return null;
    }
}
