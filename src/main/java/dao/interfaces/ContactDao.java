package dao.interfaces;

import model.Contact;

import javax.naming.NamingException;
import java.util.List;

public interface ContactDao {
    void insert(Contact contact);
    void update(Contact contact);
    void delete(Contact contact);
    List<Contact> getContactsPage(int pageNumber) throws NamingException;
}
