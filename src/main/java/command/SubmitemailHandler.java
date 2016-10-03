package command;

import dao.interfaces.CityDao;
import dao.interfaces.ContactDao;
import dao.interfaces.CountryDao;
import dao.interfaces.RelationshipDao;
import dao.mysqlimplementation.*;
import model.Contact;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import util.ContactUtils;
import util.EmailHelper;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SubmitemailHandler implements RequestHandler {
    private final static Logger LOG = LoggerFactory.getLogger(SubmitemailHandler.class);

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
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        EmailHelper emailHelper = new EmailHelper();
        String emailSubject = ContactUtils.getUTF8String(request.getParameter("subject"));
        String emailText = ContactUtils.getUTF8String(request.getParameter("text"));
        String []splitedIds = request.getParameter("id").split(",");
        try (Connection connection = MySqlConnectionFactory.getInstance().getConnection()) {
            ContactDao contactDao = new MySqlContactDao(connection);
            RelationshipDao relationshipDao = new MySqlRelationshipDao(connection);
            CountryDao countryDao = new MySqlCountryDao(connection);
            CityDao cityDao = new MySqlCityDao(connection);
            for (String id : splitedIds) {
                int contactId = Integer.parseInt(id);
                Contact contact = contactDao.getByID(contactId);
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

                try {
                    emailHelper.sendEmail(contact.getEmail(), emailSubject, message);
                } catch (MessagingException e) {
                    LOG.warn("can't send email to - ", contact.getEmail(), e);
                }
            }
        } catch (SQLException | NamingException e) {
            LOG.warn("can't get db connection");
        }
        response.sendRedirect("/contact/?action=show&page=1");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("/contact/?action=show&page=1");
    }
}
