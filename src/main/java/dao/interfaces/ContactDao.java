package dao.interfaces;

import model.Contact;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ContactDao {
    void insert(Contact contact) throws SQLException;
    void update(Connection connection, Contact contact)throws SQLException;
    void delete(Contact contact);
    void deleteByID(int id);
    Contact getByID(int id);
    int getMaxID();
    int getRowsCount();
    List<Contact> getContactsPage(int pageNumber) throws NamingException;
}
