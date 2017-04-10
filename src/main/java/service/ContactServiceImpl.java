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
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class ContactServiceImpl implements ContactService {
    private static final Logger LOG = LoggerFactory.getLogger(ContactServiceImpl.class);

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
    public List<Contact> getByLoginUser(String loginUser) throws DaoException {
        return contactDao.getByLoginUser(loginUser);
    }

    @Override
    public Page<Contact> getByLoginUser(int pageNumber, int limit, String loginUser) throws DaoException {
        return contactDao.getByLoginUser(pageNumber, limit, loginUser);
    }

    @Override
    public Page<Contact> getByLoginUser(ContactSearchCriteria searchCriteria, int pageNumber, int limit, String loginUser) throws DaoException {
        return contactDao.getByLoginUser(searchCriteria, pageNumber, limit, loginUser);
    }

    @Override
    public Contact getByIDAndLoginUser(int id, String loginUser) throws DaoException {
        return contactDao.getByIDAndLoginUser(id, loginUser);
    }

    @Override
    public List<Contact> getByIdInAndLoginUser(List<Integer> idList, String loginUser) throws DaoException {
        return contactDao.getByIdInAndLoginUser(idList, loginUser);
    }

    @Override
    public List<Contact> getByEmailNotNullAndLoginUser(String loginUser) throws DaoException {
        return contactDao.getByEmailNotNullAndLoginUser(loginUser);
    }

    @Override
    public List<Contact> getByBirthdayAndLoginUserIn(Date date, List<String> loginUserList) throws DaoException {
        return contactDao.getByBirthdayAndLoginUserIn(date, loginUserList);
    }

    @Override
    public long getMaxID() throws DaoException {
        return contactDao.getMaxID();
    }

    @Override
    public long getCountByLoginUser(String loginUser) throws DaoException {
        return contactDao.getCountByLoginUser(loginUser);
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
            contact.setLoginUser(request.getUserPrincipal().getName());

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
        String str = (String) request.getAttribute("profileImage");
        if(StringUtils.isBlank(str)) {
            LOG.info("profile image not specified");
            return result;
        }

        try {
            byte[] imagedata = DatatypeConverter.parseBase64Binary(str.substring(str.indexOf(',') + 1));

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));

            File fileToSave = ContactFileUtils.createTempFile("pri", ".png");
            ImageIO.write(bufferedImage, "png", fileToSave);
            result = "/contact/image?name=" + fileToSave.getName();
        } catch (IOException e) {
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
