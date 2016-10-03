package dao.interfaces;

import model.Contact;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ContactDao {
    void insert(Contact contact) throws SQLException;
    void update(Contact contact)throws SQLException;
    void delete(Contact contact);
    void deleteByID(int id);
    Contact getByID(int id);
    int getMaxID();
    int getRowsCount();
    List<Contact> getContactsPage(int pageNumber);
    List<Contact> find(String firstName, String lastName, String patronymic, int age1, int age2, int gender, String citizenship, int relationship, String companyName, int country, int city, String street, String postcode);
    List<Contact> getContactsWithEmail();
}
