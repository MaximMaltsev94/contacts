package dao.interfaces;

import exceptions.DaoException;
import model.ContactGroups;

import java.util.List;

public interface ContactGroupsDao {
    ContactGroups getByGroupName(String groupName);
    List<ContactGroups> getByContactID(int contactID);
    void insert(ContactGroups contactGroups) throws DaoException;
    void insert(List<ContactGroups> contactGroups) throws DaoException;
    void update(ContactGroups contactGroups) throws DaoException;
    void delete(int id) throws DaoException;
}
