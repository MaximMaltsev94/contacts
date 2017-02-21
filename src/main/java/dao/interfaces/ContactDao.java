package dao.interfaces;

import exceptions.DaoException;
import model.Contact;
import model.ContactSearchCriteria;
import model.Page;

import java.util.Date;
import java.util.List;

public interface ContactDao {
    Contact insert(Contact contact) throws DaoException;
    void update(Contact contact) throws DaoException;
    void delete(Contact contact) throws DaoException;
    void delete(List<Integer> idList) throws DaoException;
    void deleteByID(int id) throws DaoException;


    Page<Contact> getByLoginUser(int pageNumber, int limit, String loginUser) throws DaoException;
    Page<Contact> getByLoginUser(ContactSearchCriteria searchCriteria, int pageNumber, int limit, String loginUser) throws DaoException;
    Contact getByIDAndLoginUser(int id, String loginUser) throws DaoException;
    List<Contact> getByIdInAndLoginUser(List<Integer> idList, String loginUser) throws DaoException;
    long getMaxID() throws DaoException;
    long getCountByLoginUser(String loginUser) throws DaoException;
    List<Contact> getByEmailNotNullAndLoginUser(String loginUser) throws DaoException;
    List<Contact> getByBirthdayAndLoginUserIn(Date date, List<String> loginUserList) throws DaoException;
}
