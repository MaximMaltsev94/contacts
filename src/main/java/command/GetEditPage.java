package command;

import dao.implementation.*;
import dao.interfaces.*;
import exceptions.CommandExecutionException;
import exceptions.ConnectionException;
import exceptions.DaoException;
import exceptions.DataNotFoundException;
import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetEditPage implements Command {
    private final static Logger LOG = LoggerFactory.getLogger(GetEditPage.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response, Connection connection) throws CommandExecutionException, DataNotFoundException {
        String VIEW_NAME = "editContact";

        try {
            ContactDao contactDao = new ContactDaoImpl(connection);
            int contactID = Integer.parseInt(request.getParameter("id"));
            int maxID = contactDao.getMaxID();
            if (contactID < 1 || contactID > maxID) {
                throw new NumberFormatException("id out of range");
            }

            Contact contact = contactDao.getByID(contactID);
            request.setAttribute("contact", contact);

            RelationshipDao rshDao = new RelationshipDaoImpl(connection);
            List<Relationship> relationshipList = rshDao.getAll();
            request.setAttribute("relationshipList", relationshipList);

            CountryDao countryDao = new CountryDaoImpl(connection);
            List<Country> countryList = countryDao.getAll();
            request.setAttribute("countryList", countryList);

            CityDao cityDao = new CityDaoImpl(connection);
            List<City> cityList = cityDao.getAll();
            request.setAttribute("cityList", cityList);

            PhoneDao phoneDao = new PhoneDaoImpl(connection);
            List<Phone> phoneList = phoneDao.getPhoneByContactID(contactID);
            request.setAttribute("phoneList", phoneList);

            AttachmentDao attachmentDao = new AttachmentDaoImpl(connection);
            List<Attachment> attachmentList = attachmentDao.getByContactId(contactID);
            request.setAttribute("attachmentList", attachmentList);

        } catch (NumberFormatException e) {
            LOG.warn("incorrect contact id {}", request.getParameter("id"), e);
            throw new DataNotFoundException("can't find contact with specified id", e);
        }  catch (DaoException e) {
            LOG.error("error while accessing database", e);
            throw new CommandExecutionException("error while accessing database",e);
        }

        return VIEW_NAME;
    }
}
