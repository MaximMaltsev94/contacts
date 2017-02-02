package service;

import exceptions.DaoException;
import exceptions.RequestParseException;
import model.Contact;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ContactService {
    Contact parseRequest(HttpServletRequest request) throws RequestParseException;
    String parseProfileImage(HttpServletRequest request);
    void deleteProfileImageFile(Contact contact);


    void insert(Contact contact) throws DaoException;
    void update(Contact contact) throws DaoException;
    void delete(List<Integer> idList) throws DaoException;

    List<Contact> get(int pageNumber, int limit) throws DaoException;
//    List<Contact> get(SearchCriteria searchCriteria, int pageNumber, int limit) throws DaoException;
    Contact getByID(int id) throws DaoException;
    List<Contact> getByIdIn(List<Integer> idList) throws DaoException;
    long getCount() throws DaoException;
    long getMaxID() throws DaoException;
}
