package service;

import dao.implementation.ContactDaoImpl;
import dao.interfaces.ContactDao;
import exceptions.DaoException;
import exceptions.RequestParseException;
import model.Contact;
import model.ContactSearchCriteria;
import model.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactFileUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.ParseException;
import java.util.List;

public class ContactServiceImpl implements ContactService {
    private final static Logger LOG = LoggerFactory.getLogger(ContactServiceImpl.class);

    private ContactDao contactDao;

    public ContactServiceImpl(Connection connection) {
        contactDao = new ContactDaoImpl(connection);
    }

    @Override
    public Contact insert(Contact contact) throws DaoException {
        return contactDao.insert(contact);
    }

    @Override
    public void delete(List<Integer> idList) throws DaoException {
        contactDao.delete(idList);
    }

    @Override
    public void update(Contact contact) throws DaoException {
        contactDao.update(contact);
    }

    @Override
    public Page<Contact> get(int pageNumber, int limit) throws DaoException {
        return contactDao.get(pageNumber, limit);
    }

    @Override
    public Page<Contact> get(ContactSearchCriteria searchCriteria, int pageNumber, int limit) throws DaoException {
        return contactDao.get(searchCriteria, pageNumber, limit);
    }

    @Override
    public Contact getByID(int id) throws DaoException {
        return contactDao.getByID(id);
    }

    @Override
    public List<Contact> getByIdIn(List<Integer> idList) throws DaoException {
        return contactDao.getByIdIn(idList);
    }

    @Override
    public List<Contact> getByEmailNotNull() throws DaoException {
        return contactDao.getByEmailNotNull();
    }

    @Override
    public long getMaxID() throws DaoException {
        return contactDao.getMaxID();
    }

    @Override
    public long getCount() throws DaoException {
        return contactDao.getCount();
    }

    @Override
    public Contact parseRequest(HttpServletRequest request) throws RequestParseException {
        Contact contact = new Contact();
        try {
            contact.setFirstName((String) request.getAttribute("firstName"));
            contact.setLastName((String) request.getAttribute("lastName"));
            contact.setPatronymic((String) request.getAttribute("patronymic"));

            String birthDate = (String) request.getAttribute("birthDate");
            if(StringUtils.isNotBlank(birthDate)) {
                contact.setBirthDate(DateUtils.parseDate(birthDate, "dd.MM.yyyy"));
            }

            contact.setGender(request.getAttribute("gender").toString().charAt(0) == '1');
            contact.setCitizenship((String) request.getAttribute("citizenship"));
            contact.setRelationshipID(Integer.parseInt((String) request.getAttribute("relationship")));
            contact.setWebSite((String) request.getAttribute("webSite"));
            contact.setEmail((String) request.getAttribute("email"));
            contact.setCompanyName((String) request.getAttribute("companyName"));
            contact.setCountryID(Integer.parseInt((String) request.getAttribute("country")));
            contact.setCityID(Integer.parseInt((String) request.getAttribute("city")));
            contact.setStreet((String) request.getAttribute("street"));
            contact.setPostcode((String) request.getAttribute("postcode"));

            String profileImage = parseProfileImage(request);
            contact.setProfilePicture(profileImage);
        } catch (ParseException e) {
            LOG.error("birth date not matches required format. Found - \"{}\" expected \"dd.MM.yyyy\"", request.getAttribute("birthDate"));
            throw new RequestParseException("birth date not matches expected format \"dd.MM.yyyy\"", e);
        }
        return contact;
    }

    @Override
    public String parseProfileImage(HttpServletRequest request) {

        String result = "/sysImages/default.png";
        InputStream inputStream = (InputStream) request.getAttribute("profileImage");

        try {
            // TODO: 18.01.2017 add processing image to quad size
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                LOG.info("profile image not specified");
                return result;
            }
            //check if loaded file is actually image
            image.toString();


            File fileToSave = ContactFileUtils.createTempFile("pri", ".png");
            ImageIO.write(image, "png", fileToSave);

            result = "?action=image&name=" + fileToSave.getName();
        }catch (IOException e) {
            LOG.error("can't save profile image to file system", e);
        }
        return result;
    }

    @Override
    public void deleteProfileImageFile(Contact contact) {
        if(contact.getProfilePicture() != null)
            ContactFileUtils.deleteFileByUrl(contact.getProfilePicture(), "pri");
    }
}
