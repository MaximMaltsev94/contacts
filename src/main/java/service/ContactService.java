package service;

import exceptions.DaoException;
import exceptions.RequestParseException;
import model.Contact;
import model.ContactSearchCriteria;
import model.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ContactService {
    Contact parseRequest(HttpServletRequest request) throws RequestParseException;
    String parseProfileImage(HttpServletRequest request);
    void deleteProfileImageFile(Contact contact);


    Contact insert(Contact contact) throws DaoException;
    void update(Contact contact) throws DaoException;
    void delete(List<Integer> idList) throws DaoException;

    Page<Contact> getByLoginUser(int pageNumber, int limit, String loginUser) throws DaoException;
    Page<Contact> getByLoginUser(ContactSearchCriteria searchCriteria, int pageNumber, int limit, String loginUser) throws DaoException;
    Contact getByIDAndLoginUser(int id, String loginUser) throws DaoException;
    List<Contact> getByIdInAndLoginUser(List<Integer> idList, String loginUser) throws DaoException;
    List<Contact> getByEmailNotNullAndLoginUser(String loginUser) throws DaoException;
    long getCountByLoginUser(String loginUser) throws DaoException;
    long getMaxID() throws DaoException;
}
