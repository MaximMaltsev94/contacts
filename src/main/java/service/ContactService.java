package service;

import com.vk.api.sdk.objects.friends.UserXtrLists;
import exceptions.DaoException;
import exceptions.RequestParseException;
import model.Contact;
import model.ContactSearchCriteria;
import model.Page;
import model.UserGroups;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ContactService {
    Contact parseRequest(HttpServletRequest request) throws RequestParseException;
    String parseProfileImage(HttpServletRequest request);
    void deleteProfileImageFile(Contact contact);
    List<Contact> mapVkUserToContact(List<UserXtrLists> friendList, String loginUser);


    Contact insert(Contact contact) throws DaoException;
    void update(Contact contact) throws DaoException;
    void delete(List<Integer> idList) throws DaoException;

    List<Contact> getByLoginUser(String loginUser) throws DaoException;
    Page<Contact> getByLoginUser(int pageNumber, int limit, String loginUser) throws DaoException;
    Page<Contact> getByLoginUser(ContactSearchCriteria searchCriteria, int pageNumber, int limit, String loginUser) throws DaoException;

    Contact getByIDAndLoginUser(int id, String loginUser) throws DaoException;

    List<Contact> getByIdInAndLoginUser(List<Integer> idList, String loginUser) throws DaoException;
    Page<Contact> getByIdInAndLoginUser(int pageNumber, int limit, List<Integer> idList, String loginUser) throws DaoException;

    List<Contact> getByEmailNotNullAndLoginUser(String loginUser) throws DaoException;
    List<Contact> getByBirthdayAndLoginUserIn(Date date, List<String> loginUserList) throws DaoException;

    Map<Integer, List<UserGroups>> getContactGroups(List<Integer> contactIdList) throws DaoException;
    long getCountByLoginUser(String loginUser) throws DaoException;
    long getMaxID() throws DaoException;
}
