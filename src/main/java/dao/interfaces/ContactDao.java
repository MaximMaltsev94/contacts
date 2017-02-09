package dao.interfaces;

import exceptions.DaoException;
import model.Contact;
import model.ContactSearchCriteria;

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


    List<Contact> get(int pageNumber, int limit) throws DaoException;
    List<Contact> get(ContactSearchCriteria searchCriteria, int pageNumber, int limit) throws DaoException;
    Contact getByID(int id) throws DaoException;
    List<Contact> getByIdIn(List<Integer> idList) throws DaoException;
    long getMaxID() throws DaoException;
    long getCount() throws DaoException;
    List<Contact> getByEmailNotNull() throws DaoException;
    List<Contact> getByBirthdayToday() throws DaoException;
}
