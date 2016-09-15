package dao.interfaces;

import model.Contact;

import java.util.List;

public interface ContactDao {
    void insert(Contact contact);
    void update(Contact contact);
    void delete(Contact contact);
    List<Contact> getContactsPage(int pageNumber);
}
