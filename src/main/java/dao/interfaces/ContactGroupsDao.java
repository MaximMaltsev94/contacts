package dao.interfaces;

import exceptions.DaoException;
import model.ContactGroups;

import java.util.List;

public interface ContactGroupsDao {
    List<ContactGroups> getByGroupId(int groupId) throws DaoException;
}
