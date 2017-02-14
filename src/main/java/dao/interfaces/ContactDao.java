package dao.interfaces;

import exceptions.DaoException;
import model.Contact;
import model.ContactSearchCriteria;
import model.Page;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
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

    // TODO: 14.02.2017 refactor to select by login user
    List<Contact> getByBirthdayToday() throws DaoException;
}
