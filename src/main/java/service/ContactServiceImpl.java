package service;

import dao.implementation.ContactDaoImpl;
import dao.interfaces.ContactDao;
import exceptions.DaoException;
import exceptions.RequestParseException;
import model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ContactFileUtils;
import util.ContactUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContactServiceImpl implements ContactService {
    private static final Logger LOG = LoggerFactory.getLogger(ContactServiceImpl.class);

    private ContactDao contactDao;
    private ContactGroupsService contactGroupsService;
    private UserGroupsService userGroupsService;

    public ContactServiceImpl(Connection connection) {
        contactDao = new ContactDaoImpl(connection);
        contactGroupsService = new ContactGroupsServiceImpl(connection);
        userGroupsService = new UserGroupsServiceImpl(connection);
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
    public Page<Contact> getByIdInAndLoginUser(int pageNumber, int limit, List<Integer> idList, String loginUser) throws DaoException {
        return contactDao.getByIdInAndLoginUser(pageNumber, limit, idList, loginUser);
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
    public Map<Integer, List<UserGroups>> getContactGroups(List<Integer> contactIdList) throws DaoException {
        List<ContactGroups> contactGroupsList = contactGroupsService.getByContactIdIn(contactIdList);
        List<Integer> groupIdList = contactGroupsList.stream()
                .map(ContactGroups::getGroupId)
                .collect(Collectors.toList());

        Map<Integer, UserGroups> userGroupsMap = userGroupsService.getByIdIn(groupIdList).stream()
                .collect(Collectors.toMap(UserGroups::getId, o -> o));

        return contactGroupsList.stream()
                .collect(Collectors.groupingBy(ContactGroups::getContactID, Collectors.mapping(o -> userGroupsMap.get(o.getGroupId()), Collectors.toList())));
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

        contact.setFirstName((String) request.getAttribute("firstName"));
        contact.setLastName((String) request.getAttribute("lastName"));
        contact.setPatronymic((String) request.getAttribute("patronymic"));

        contact.setBirthDay(Integer.parseInt((String) request.getAttribute("birth_day")));
        contact.setBirthMonth(Integer.parseInt((String) request.getAttribute("birth_month")));
        contact.setBirthYear(Integer.parseInt((String) request.getAttribute("birth_year")));

        contact.setGender(Integer.parseInt((String)request.getAttribute("gender")));
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
        return contact;
    }

    @Override
    public String parseProfileImage(HttpServletRequest request) {

        String result = "/sysImages/default.png";
        if(request.getAttribute("gender") != null && ContactUtils.GENDER_WOMAN == Integer.parseInt((String) request.getAttribute("gender"))){
            result = "/sysImages/girl.png";
        }
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
