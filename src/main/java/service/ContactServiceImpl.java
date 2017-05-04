package service;

import com.vk.api.sdk.objects.users.UserFull;
import dao.implementation.ContactDaoImpl;
import dao.interfaces.ContactDao;
import exceptions.DaoException;
import exceptions.InvalidUrlException;
import exceptions.RequestParseException;
import model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.BufferedImageSaver;
import util.ContactFileUtils;
import util.ContactUtils;
import util.RemoteImageDownloader;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    public List<Integer> insert(List<Contact> contactList) throws DaoException {
        return contactDao.insert(contactList);
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

    private String concatProfileImageUrl(String fileName) {
        return "/contact/image?name=" + fileName;
    }

    @Override
    public String parseProfileImage(HttpServletRequest request) {

        String result = ContactUtils.DEFAULT_MAN_AVATAR;
        if(request.getAttribute("gender") != null && ContactUtils.GENDER_WOMAN == Integer.parseInt((String) request.getAttribute("gender"))){
            result = ContactUtils.DEFAULT_WOMAN_AVATAR;
        }
        String str = (String) request.getAttribute("profileImage");
        if(StringUtils.isBlank(str)) {
            LOG.info("profile image not specified");
            return result;
        }

        try {
            byte[] imagedata = DatatypeConverter.parseBase64Binary(str.substring(str.indexOf(',') + 1));

            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));

            String fileName = new BufferedImageSaver(bufferedImage).saveToFileSystem("pri");
            result = concatProfileImageUrl(fileName);
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

    @Override
    public List<Contact> mapVkFriendToContact(List<? extends UserFull> friendList, String loginUser) {
        return friendList
                .stream()
                .filter(e -> Objects.isNull(e.getDeactivated()))
                .map(friend -> {
                    Contact contact = new Contact();
                    contact.setId(friend.getId());
                    contact.setFirstName(friend.getFirstName());
                    contact.setLastName(friend.getLastName());
                    contact.setPatronymic(friend.getNickname());
                    switch (friend.getSex()) {
                        case UNKNOWN:
                            contact.setGender(ContactUtils.GENDER_ANY);
                            break;
                        case MALE:
                            contact.setGender(ContactUtils.GENDER_MAN);
                            break;
                        case FEMALE:
                            contact.setGender(ContactUtils.GENDER_WOMAN);
                            break;
                    }

                    String db[] = StringUtils.split(friend.getBdate(), ".");
                    if(db != null) {
                        if (db.length > 0)
                            contact.setBirthDay(Integer.parseInt(db[0]));
                        if (db.length > 1)
                            contact.setBirthMonth(Integer.parseInt(db[1]));
                        if (db.length > 2)
                            contact.setBirthYear(Integer.parseInt(db[2]));
                    }

                    contact.setRelationshipID(friend.getRelation() == null ? 0 : friend.getRelation());
//                        contact.setCitizenship();
                    contact.setWebSite(friend.getSite());
//                        contact.setEmail();
                    if(CollectionUtils.isNotEmpty(friend.getCareer())) {
                        contact.setCompanyName(friend.getCareer().get(friend.getCareer().size() - 1).getCompany());
                    }
                    if(friend.getPhoto200() == null) {
                        if(contact.getGender() == ContactUtils.GENDER_WOMAN) {
                            contact.setProfilePicture(ContactUtils.DEFAULT_WOMAN_AVATAR);
                        } else {
                            contact.setProfilePicture(ContactUtils.DEFAULT_MAN_AVATAR);
                        }
                    } else {
                        contact.setProfilePicture(friend.getPhoto200());
                    }
                    if(friend.getCountry() != null) {
                        contact.setCountryID(friend.getCountry().getId());
                    }
                    if(friend.getCity() != null) {
                        contact.setCityID(friend.getCity().getId());
                    }
//                        contact.setStreet(friend.get);
//                        contact.setPostcode();
                    contact.setLoginUser(loginUser);
                    return contact;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void saveRemoteImages(List<Contact> contactList) {
        for (Contact contact : contactList) {
            if(!ContactUtils.DEFAULT_MAN_AVATAR.equals(contact.getProfilePicture()) && !ContactUtils.DEFAULT_WOMAN_AVATAR.equals(contact.getProfilePicture())) {
                try {
                    BufferedImage image = new RemoteImageDownloader(contact.getProfilePicture()).download();
                    String fileName = new BufferedImageSaver(image).saveToFileSystem("pri");
                    contact.setProfilePicture(concatProfileImageUrl(fileName));
                } catch (IOException e) {
                    LOG.error("can;t save image to file system", e);
                } catch (InvalidUrlException e) {
                    LOG.error("invalid url for image saving - ", contact.getProfilePicture());
                }
            }
        }
    }
}
