package dao.interfaces;

import exceptions.DaoException;
import model.ContactGroups;

import java.util.List;

public interface ContactGroupsDao {
    List<ContactGroups> getByGroupId(int groupId) throws DaoException;
    List<ContactGroups> getByContactId(int contactId) throws DaoException;
    List<ContactGroups> getByContactIdIn(List<Integer> contactIdList) throws DaoException;
    void insert(List<ContactGroups> contactGroupsList) throws DaoException;
    void deleteByContactId(int contactId) throws DaoException;
    void deleteByGroupId(int groupId) throws DaoException;
}
