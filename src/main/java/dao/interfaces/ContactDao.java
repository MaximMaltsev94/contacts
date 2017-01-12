package dao.interfaces;

import exceptions.DaoException;
import model.Contact;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ContactDao {
    // TODO: 12.01.2017 overload method getAll to accepting pagable or nothing parameters
    void insert(Contact contact) throws DaoException;
    void update(Contact contact) throws DaoException;
    void delete(Contact contact) throws DaoException;
    void deleteByID(int id) throws DaoException;
    Contact getByID(int id) throws DaoException;
    int getMaxID() throws DaoException;
    int getRowsCount() throws DaoException;
    List<Contact> getContactsPage(int pageNumber) throws DaoException;
    List<Contact> find(String firstName, String lastName, String patronymic, int age1, int age2, int gender, String citizenship, int relationship, String companyName, int country, int city, String street, String postcode) throws DaoException;
    List<Contact> getContactsWithEmail() throws DaoException;
    List<Contact> getByBirthdayToday() throws DaoException;
}
