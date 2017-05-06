package service;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.UserFull;
import exceptions.DaoException;
import exceptions.RequestParseException;
import model.Contact;
import model.ContactSearchCriteria;
import model.Page;
import model.UserGroups;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ContactService {
    Contact parseRequest(HttpServletRequest request) throws RequestParseException;
    String parseProfileImage(HttpServletRequest request);
    void deleteProfileImageFile(Contact contact);
    List<Contact> mapVkFriendToContact(List<? extends UserFull> friendList, String loginUser);
    void saveRemoteImages(List<Contact> contactList);
    File writeContactsToExcel(String loginUser) throws IOException;


    Contact insert(Contact contact) throws DaoException;
    List<Integer> insert(List<Contact> contactList) throws DaoException;
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

    List<Contact> getByVkIdNotNullAndLoginUser(String loginUser) throws DaoException;
    List<? extends UserFull> getVkPart(UserActor userActor, int pageNumber, int count, String loginUser) throws DaoException, ClientException, ApiException;
}
